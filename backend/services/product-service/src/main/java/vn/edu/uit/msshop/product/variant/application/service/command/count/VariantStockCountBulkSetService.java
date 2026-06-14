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
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantStockCountBulkSetCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantStockCountBulkSetUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Service
@RequiredArgsConstructor
class VariantStockCountBulkSetService
        implements VariantStockCountBulkSetUseCase {

    private final VariantStockCountBulkLookupByVariantIdsPort stockCountBulkLookupByIdsPort;
    private final VariantStockCountBulkUpdatePort stockCountBulkUpdatePort;
    private final IncreaseProductStockCountsPort increaseProductStockCountsPort;
    private final DecreaseProductStockCountsPort decreaseProductStockCountsPort;

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
            final VariantStockCountBulkSetCommand cmd) {
        final var rawNewValueByVariantId = cmd.stockCountByVariantId();
        if (rawNewValueByVariantId.isEmpty()) {
            return;
        }
        final var newValueById = VariantStockCountBulkSetService.toNewValueByVariantId(rawNewValueByVariantId);

        final var changeList = this.loadChangeList(newValueById);
        this.persistUpdates(changeList);
        this.propagateDeltas(changeList);
    }

    private static Map<VariantId, VariantStockCountValue> toNewValueByVariantId(
            final Map<UUID, Integer> rawNewValueByVariantId) {
        final var newValueByVariantId = HashMap.<VariantId, VariantStockCountValue>newHashMap(
                rawNewValueByVariantId.size());
        for (final var entry : rawNewValueByVariantId.entrySet()) {
            final var variantId = new VariantId(entry.getKey());
            final var variantStockCountValue = new VariantStockCountValue(entry.getValue());

            newValueByVariantId.put(
                    variantId,
                    variantStockCountValue);
        }
        return newValueByVariantId;
    }

    private List<StockCountChange> loadChangeList(
            final Map<VariantId, VariantStockCountValue> newValueByVariantId) {
        final var variantIdSet = newValueByVariantId.keySet();
        final var amountVariant = variantIdSet.size();
        final var currentByVariantId = this.stockCountBulkLookupByIdsPort.loadAllByVariantIds(variantIdSet);

        final var changeList = new ArrayList<StockCountChange>(amountVariant);
        for (final var entry : newValueByVariantId.entrySet()) {
            final var variantId = entry.getKey();
            final var newValue = entry.getValue();

            final var current = currentByVariantId.get(variantId);
            if (current == null) {
                throw new VariantNotFoundException(variantId);
            }

            final var change = new StockCountChange(current, newValue);
            changeList.add(change);
        }
        return changeList;
    }

    private void persistUpdates(
            final List<StockCountChange> changeList) {
        final var updatedCounts = new ArrayList<VariantStockCount>(changeList.size());
        for (final var change : changeList) {
            updatedCounts.add(change.updatedCount());
        }
        this.stockCountBulkUpdatePort.updateAll(updatedCounts);
    }

    private void propagateDeltas(
            final List<StockCountChange> changeList) {
        final var deltas = VariantStockCountBulkSetService.toDeltasByProductId(changeList);

        if (!deltas.increments().isEmpty()) {
            this.increaseProductStockCountsPort.increaseAllStockCounts(deltas.increments());
        }
        if (!deltas.decrements().isEmpty()) {
            this.decreaseProductStockCountsPort.decreaseAllStockCounts(deltas.decrements());
        }
    }

    private static DeltasByProductId toDeltasByProductId(
            final List<StockCountChange> changeList) {
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

    private record StockCountChange(
            VariantStockCount current,
            VariantStockCountValue newValue) {

        VariantStockCount updatedCount() {
            return new VariantStockCount(
                    this.current.getVariantId(),
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
