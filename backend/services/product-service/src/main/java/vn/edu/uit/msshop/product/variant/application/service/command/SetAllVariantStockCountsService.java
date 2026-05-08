package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantStockCountsCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetAllVariantStockCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantInventoryStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Service
@RequiredArgsConstructor
public class SetAllVariantStockCountsService implements SetAllVariantStockCountsUseCase {
    private final LoadAllVariantStockCountsPort loadAllPort;
    private final UpdateAllVariantStockCountsPort updateAllPort;
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
    public void execute(
            final SetAllVariantStockCountsCommand command) {
        final var stockCounts = command.stockCounts();
        if (stockCounts.isEmpty()) {
            return;
        }

        final var resolved = this.resolve(stockCounts);
        this.persistUpdates(resolved);
        this.propagateIncrements(resolved);
    }

    private List<ResolvedStockCount> resolve(
            final List<VariantInventoryStockCount> stockCounts) {
        final var variantIds = stockCounts.stream()
                .map(VariantInventoryStockCount::variantId)
                .collect(Collectors.toUnmodifiableSet());
        final var currentByVariantId = this.loadAllPort.loadAllByIds(variantIds);

        return stockCounts.stream()
                .map(inventory -> SetAllVariantStockCountsService.resolveOne(inventory, currentByVariantId))
                .toList();
    }

    private static ResolvedStockCount resolveOne(
            final VariantInventoryStockCount inventory,
            final Map<VariantId, VariantStockCount> currentByVariantId) {
        final var current = currentByVariantId.get(inventory.variantId());
        if (current == null) {
            throw new VariantNotFoundException(inventory.variantId());
        }
        return new ResolvedStockCount(current, inventory.value());
    }

    private void persistUpdates(
            final List<ResolvedStockCount> resolved) {
        final var updated = resolved.stream()
                .map(ResolvedStockCount::toUpdated).toList();
        this.updateAllPort.updateAll(updated);
    }

    private void propagateIncrements(
            final List<ResolvedStockCount> resolved) {
        final var deltas = SetAllVariantStockCountsService.toDeltasByProductId(resolved);

        if (!deltas.increments().isEmpty()) {
            this.increaseProductStockCountsPort.increaseAllStockCounts(deltas.increments());
        }
        if (!deltas.decrements().isEmpty()) {
            this.decreaseProductStockCountsPort.decreaseAllStockCounts(deltas.decrements());
        }
    }

    private static DeltasByProductId toDeltasByProductId(
            final List<ResolvedStockCount> resolved) {
        final var incrementByProductId = HashMap.<VariantProductId, Integer>newHashMap(resolved.size());
        final var decrementByProductId = HashMap.<VariantProductId, Integer>newHashMap(resolved.size());

        for (final var item : resolved) {
            final var delta = item.delta();
            if (delta == 0) {
                continue;
            }

            final var productId = item.current().getProductId();
            if (delta > 0) {
                incrementByProductId.merge(productId, delta, Integer::sum);
            } else {
                decrementByProductId.merge(productId, -delta, Integer::sum);
            }
        }

        return new DeltasByProductId(incrementByProductId, decrementByProductId);
    }

    private record ResolvedStockCount(
            VariantStockCount current,
            VariantStockCountValue newValue) {

        VariantStockCount toUpdated() {
            return new VariantStockCount(
                    this.current.getVariantId(),
                    this.current.getProductId(),
                    this.newValue);
        }

        int delta() {
            return this.newValue.value() - this.current.getValue().value();
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
