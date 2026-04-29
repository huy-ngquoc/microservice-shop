package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantStockCountsCommand;
import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantInventoryStockCount;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetAllVariantStockCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Service
@RequiredArgsConstructor
public class SetAllVariantStockCountsService
        implements SetAllVariantStockCountsUseCase {
    private final LoadAllVariantStockCountsPort loadAllPort;
    private final UpdateAllVariantStockCountsPort updateAllPort;
    private final IncreaseProductStockCountsPort increaseProductStockCountsPort;

    @Override
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
                .map(inventory -> SetAllVariantStockCountsService
                        .resolveOne(inventory, currentByVariantId))
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
                .map(ResolvedStockCount::toUpdated)
                .toList();
        this.updateAllPort.updateAll(updated);
    }

    private void propagateIncrements(
            final List<ResolvedStockCount> resolved) {
        final var incrementByProductId = SetAllVariantStockCountsService
                .toIncrementByProductId(resolved);
        if (incrementByProductId.isEmpty()) {
            return;
        }
        this.increaseProductStockCountsPort.increaseAllStockCounts(incrementByProductId);
    }

    private static Map<VariantProductId, Integer> toIncrementByProductId(
            final List<ResolvedStockCount> resolved) {
        final var byProductId = HashMap.<VariantProductId, Integer>newHashMap(resolved.size());
        for (final var item : resolved) {
            final var delta = item.delta();
            if (delta != 0) {
                final var productId = item.current().getProductId();
                final var current = byProductId.getOrDefault(productId, 0);

                byProductId.put(productId, current + delta);
            }
        }
        return byProductId;
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
}
