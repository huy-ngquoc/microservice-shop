package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetAllVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Service
@RequiredArgsConstructor
public class SetAllVariantSoldCountsService
        implements SetAllVariantSoldCountsUseCase {
    private final LoadAllVariantSoldCountsPort loadAllSoldCountsPort;
    private final UpdateAllVariantSoldCountsPort updateAllSoldCountsPort;
    private final IncreaseProductSoldCountsPort increaseProductSoldCountsPort;

    @Override
    @Transactional
    public void execute(
            final Collection<VariantOrderSoldCount> orderSoldCounts) {
        if (orderSoldCounts.isEmpty()) {
            return;
        }

        final var resolved = this.resolve(orderSoldCounts);
        this.persistUpdates(resolved);
        this.propagateIncrements(resolved);
    }

    private List<ResolvedSoldCount> resolve(
            final Collection<VariantOrderSoldCount> orderSoldCounts) {
        final var variantIds = orderSoldCounts.stream()
                .map(VariantOrderSoldCount::variantId)
                .collect(Collectors.toUnmodifiableSet());
        final var currentByVariantId = this.loadAllSoldCountsPort.loadAllByIds(variantIds);

        return orderSoldCounts.stream()
                .map(order -> SetAllVariantSoldCountsService.resolveOne(order, currentByVariantId))
                .toList();
    }

    private static ResolvedSoldCount resolveOne(
            final VariantOrderSoldCount order,
            final Map<VariantId, VariantSoldCount> currentByVariantId) {
        final var current = currentByVariantId.get(order.variantId());
        if (current == null) {
            throw new VariantNotFoundException(order.variantId());
        }
        return new ResolvedSoldCount(current, order.value());
    }

    private void persistUpdates(
            final List<ResolvedSoldCount> resolved) {
        final var updated = resolved.stream()
                .map(ResolvedSoldCount::toUpdated)
                .toList();
        this.updateAllSoldCountsPort.updateAll(updated);
    }

    private void propagateIncrements(
            final List<ResolvedSoldCount> resolved) {
        final var incrementByProductId = SetAllVariantSoldCountsService.toIncrementByProductId(resolved);
        if (incrementByProductId.isEmpty()) {
            return;
        }
        this.increaseProductSoldCountsPort.increaseAllSoldCounts(incrementByProductId);
    }

    private static Map<VariantProductId, Integer> toIncrementByProductId(
            final List<ResolvedSoldCount> resolved) {
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

    private record ResolvedSoldCount(
            VariantSoldCount current,
            VariantSoldCountValue newValue) {

        VariantSoldCount toUpdated() {
            return new VariantSoldCount(
                    this.current.getId(),
                    this.current.getProductId(),
                    this.newValue);
        }

        int delta() {
            return this.newValue.value() - this.current.getValue().value();
        }
    }
}
