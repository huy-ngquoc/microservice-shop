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
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Service
@RequiredArgsConstructor
class VariantSoldCountBulkSetService
        implements VariantSoldCountBulkSetUseCase {

    private final LoadAllVariantSoldCountsPort loadAllSoldCountsPort;
    private final UpdateAllVariantSoldCountsPort updateAllSoldCountsPort;
    private final IncreaseProductSoldCountsPort increaseProductSoldCountsPort;
    private final DecreaseProductSoldCountsPort decreaseProductSoldCountsPort;

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
    public void execute(
            final VariantSoldCountBulkSetCommand cmd) {
        final var rawNewValueById = cmd.soldCountById();
        if (rawNewValueById.isEmpty()) {
            return;
        }
        final var newValueById = VariantSoldCountBulkSetService.toNewValueById(rawNewValueById);

        final var changeList = this.loadChangeList(newValueById);
        this.persistUpdates(changeList);
        this.propagateDeltas(changeList);
    }

    private static Map<VariantId, VariantSoldCountValue> toNewValueById(
            final Map<UUID, Integer> rawNewValueById) {
        final var newValueById = HashMap.<VariantId, VariantSoldCountValue>newHashMap(
                rawNewValueById.size());
        for (final var entry : rawNewValueById.entrySet()) {
            final var variantId = new VariantId(entry.getKey());
            final var variantSoldCountValue = new VariantSoldCountValue(entry.getValue());

            newValueById.put(
                    variantId,
                    variantSoldCountValue);
        }
        return newValueById;
    }

    private List<SoldCountChange> loadChangeList(
            final Map<VariantId, VariantSoldCountValue> newValueById) {
        final var idSet = newValueById.keySet();
        final var amountVariant = idSet.size();
        final var currentById = this.loadAllSoldCountsPort.loadAllByIds(idSet);

        final var changeList = new ArrayList<SoldCountChange>(amountVariant);
        for (final var entry : newValueById.entrySet()) {
            final var variantId = entry.getKey();
            final var newValue = entry.getValue();

            final var current = currentById.get(variantId);
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
        this.updateAllSoldCountsPort.updateAll(updatedCounts);
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
                    this.current.getId(),
                    this.current.getProductId(),
                    this.newValue);
        }

        int delta() {
            return this.newValue.value() - this.current.getValue().value();
        }

        VariantProductId productId() {
            return this.current.getProductId();
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
