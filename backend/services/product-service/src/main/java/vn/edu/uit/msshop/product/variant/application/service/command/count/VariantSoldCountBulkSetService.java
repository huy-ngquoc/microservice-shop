package vn.edu.uit.msshop.product.variant.application.service.command.count;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantSoldCountBulkSetCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantSoldCountBulkSetUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.VariantToProductSoldCountBulkDecrementPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.VariantToProductSoldCountBulkIncrementPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoldCountBulkUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoldCountBulkUpdatedEvent.VariantSoldCountDelta;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Service
@RequiredArgsConstructor
class VariantSoldCountBulkSetService
        implements VariantSoldCountBulkSetUseCase {

    private final VariantSoldCountBulkLookupByVariantIdsPort soldCountBulkLookupByIdsPort;
    private final VariantSoldCountBulkUpdatePort soldCountBulkUpdatePort;
    private final VariantToProductSoldCountBulkIncrementPort increaseProductSoldCountsPort;
    private final VariantToProductSoldCountBulkDecrementPort decreaseProductSoldCountsPort;

    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT_LIST,
                            allEntries = true)
            })
    public void setAll(
            final VariantSoldCountBulkSetCommand cmd) {
        final var rawNewValueByVariantId = cmd.soldCountByVariantId();
        if (rawNewValueByVariantId.isEmpty()) {
            return;
        }
        final var newValueByVariantId = VariantSoldCountBulkSetService
                .toNewValueByVariantId(rawNewValueByVariantId);

        final var changeList = this.loadChangeList(newValueByVariantId);
        this.persistUpdates(changeList);
        this.propagateDeltas(changeList);
        this.publishSoldCountChanges(changeList);
    }

    private static Map<VariantId, VariantSoldCountValue> toNewValueByVariantId(
            final Map<UUID, Integer> rawNewValueByVariantId) {
        final var newValueByVariantId = HashMap.<VariantId, VariantSoldCountValue>newHashMap(
                rawNewValueByVariantId.size());
        for (final var entry : rawNewValueByVariantId.entrySet()) {
            final var variantId = new VariantId(entry.getKey());
            final var variantSoldCountValue = new VariantSoldCountValue(entry.getValue());

            newValueByVariantId.put(
                    variantId,
                    variantSoldCountValue);
        }
        return newValueByVariantId;
    }

    private List<SoldCountChange> loadChangeList(
            final Map<VariantId, VariantSoldCountValue> newValueByVariantId) {
        final var variantIdSet = newValueByVariantId.keySet();
        final var amountVariant = variantIdSet.size();
        final var currentByVariantId = this.soldCountBulkLookupByIdsPort.loadAllByVariantIds(variantIdSet);

        final var changeList = new ArrayList<SoldCountChange>(amountVariant);
        for (final var entry : newValueByVariantId.entrySet()) {
            final var variantId = entry.getKey();
            final var newValue = entry.getValue();

            final var current = currentByVariantId.get(variantId);
            if (current == null) {
                throw new VariantNotFoundException(variantId);
            }

            final var change = new SoldCountChange(current, newValue);
            changeList.add(change);
        }
        return changeList;
    }

    private void persistUpdates(
            final List<SoldCountChange> changeList) {
        final var updatedCounts = changeList.stream()
                .map(SoldCountChange::updatedCount)
                .toList();
        this.soldCountBulkUpdatePort.updateAll(updatedCounts);
    }

    private void propagateDeltas(
            final List<SoldCountChange> changeList) {
        final var deltas = VariantSoldCountBulkSetService.toDeltasByProductId(changeList);

        if (!deltas.increments().isEmpty()) {
            this.increaseProductSoldCountsPort.increaseAllSoldCounts(deltas.increments());
        }
        if (!deltas.decrements().isEmpty()) {
            this.decreaseProductSoldCountsPort.decreaseAllSoldCounts(deltas.decrements());
        }
    }

    private void publishSoldCountChanges(
            final List<SoldCountChange> changeList) {
        final var deltaList = changeList.stream()
                .filter(change -> change.delta() != 0)
                .map(SoldCountChange::toDelta)
                .toList();
        if (deltaList.isEmpty()) {
            return;
        }

        final var event = new VariantSoldCountBulkUpdatedEvent(deltaList);
        this.eventPublicationPort.publishEvent(event);
    }

    private static DeltasByProductId toDeltasByProductId(
            final List<SoldCountChange> changeList) {
        final var amountVariant = changeList.size();

        final var incrementByProductId = HashMap.<VariantProductId, Integer>newHashMap(amountVariant);
        final var decrementByProductId = HashMap.<VariantProductId, Integer>newHashMap(amountVariant);

        for (final var change : changeList) {
            final var delta = change.delta();
            if (delta == 0) {
                continue;
            }

            final var productId = change.productId();
            if (delta > 0) {
                incrementByProductId.merge(productId, delta, Integer::sum);
            } else {
                decrementByProductId.merge(productId, -delta, Integer::sum);
            }
        }

        return new DeltasByProductId(
                incrementByProductId,
                decrementByProductId);
    }

    private record SoldCountChange(
            VariantSoldCount current,
            VariantSoldCountValue newValue) {

        VariantSoldCount updatedCount() {
            return new VariantSoldCount(
                    this.current.getVariantId(),
                    this.current.getProductId(),
                    this.newValue);
        }

        VariantProductId productId() {
            return this.current.getProductId();
        }

        int delta() {
            return this.newValue.value() - this.current.getValue().value();
        }

        VariantSoldCountDelta toDelta() {
            return new VariantSoldCountDelta(
                    this.current.getVariantId().value(),
                    this.current.getProductId().value(),
                    this.delta());
        }
    }

    private record DeltasByProductId(
            Map<VariantProductId, Integer> increments,
            Map<VariantProductId, Integer> decrements) {
        DeltasByProductId {
            increments = Map.copyOf(increments);
            decrements = Map.copyOf(decrements);
        }
    }
}
