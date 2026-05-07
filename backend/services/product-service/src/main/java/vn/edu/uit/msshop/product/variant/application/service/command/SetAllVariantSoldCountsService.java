package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.Collection;
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
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetAllVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductSoldCountsPort;
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
            final Collection<VariantOrderSoldCount> orderSoldCounts) {
        if (orderSoldCounts.isEmpty()) {
            return;
        }

        final var resolved = this.resolve(orderSoldCounts);
        this.persistUpdates(resolved);
        this.propagateDeltas(resolved);
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

    private void propagateDeltas(
            final List<ResolvedSoldCount> resolved) {
        final var deltas = SetAllVariantSoldCountsService.toDeltasByProductId(resolved);

        if (!deltas.increments().isEmpty()) {
            this.increaseProductSoldCountsPort.increaseAllSoldCounts(deltas.increments());
        }
        if (!deltas.decrements().isEmpty()) {
            this.decreaseProductSoldCountsPort.decreaseAllSoldCounts(deltas.decrements());
        }
    }

    private static DeltasByProductId toDeltasByProductId(
            final List<ResolvedSoldCount> resolved) {
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

        return new DeltasByProductId(
                incrementByProductId,
                decrementByProductId);
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

    private record DeltasByProductId(
            Map<VariantProductId, Integer> increments,
            Map<VariantProductId, Integer> decrements) {
        DeltasByProductId {
            increments = Map.copyOf(increments);
            decrements = Map.copyOf(decrements);
        }
    }
}
