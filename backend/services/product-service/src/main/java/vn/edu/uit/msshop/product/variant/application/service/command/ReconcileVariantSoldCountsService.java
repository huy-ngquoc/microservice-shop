package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantOrderSoldCount;
import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantProductSoldCountReconciliation;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.ReconcileVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchOrderSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.ReconcileProductSoldCountsForVariantPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCount;

@Service
@RequiredArgsConstructor
public class ReconcileVariantSoldCountsService
        implements ReconcileVariantSoldCountsUseCase {
    private final FetchOrderSoldCountsPort fetchOrderSoldCountsPort;
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final ReconcileProductSoldCountsForVariantPort reconcileProductSoldCountsPort;

    @Override
    @Transactional
    public void execute() {
        final var orderSoldCounts = this.fetchOrderSoldCountsPort.fetchAll();
        if (orderSoldCounts.isEmpty()) {
            return;
        }

        final var variantIds = orderSoldCounts.stream()
                .map(VariantOrderSoldCount::variantId)
                .toList();
        final var loadedById = this.loadVariants(variantIds);

        final var updated = ReconcileVariantSoldCountsService
                .applyNewCounts(loadedById, orderSoldCounts);
        this.updateAllPort.updateAll(updated);

        final var productReconciliations = ReconcileVariantSoldCountsService
                .toProductReconciliations(loadedById, orderSoldCounts);
        this.reconcileProductSoldCountsPort.reconcileSoldCounts(productReconciliations);
    }

    private static List<Variant> applyNewCounts(
            final Map<VariantId, Variant> loadedById,
            final Collection<VariantOrderSoldCount> orderSoldCounts) {
        return orderSoldCounts.stream()
                .filter(sc -> loadedById.containsKey(sc.variantId()))
                .map(sc -> ReconcileVariantSoldCountsService
                        .applyNewCount(sc, loadedById))
                .toList();
    }

    private static Variant applyNewCount(
            final VariantOrderSoldCount orderSoldCount,
            final Map<VariantId, Variant> loadedById) {
        final var variant = loadedById.get(orderSoldCount.variantId());
        if (variant == null) {
            throw new VariantNotFoundException(orderSoldCount.variantId());
        }

        return ReconcileVariantSoldCountsService.withNewSoldCount(
                loadedById.get(orderSoldCount.variantId()),
                orderSoldCount.soldCount());
    }

    private static Variant withNewSoldCount(
            final Variant current,
            final int newSoldCount) {
        return new Variant(
                current.getId(),
                current.getProductId(),
                current.getPrice(),
                new VariantSoldCount(newSoldCount),
                current.getTraits(),
                current.getTargets(),
                current.getImageKey(),
                current.getVersion(),
                current.getDeletionTime());
    }

    private static List<VariantProductSoldCountReconciliation> toProductReconciliations(
            final Map<VariantId, Variant> loadedById,
            final Collection<VariantOrderSoldCount> orderSoldCounts) {
        final var byProductId = new HashMap<VariantProductId, Integer>();
        for (final var sc : orderSoldCounts) {
            final var variant = loadedById.get(sc.variantId());
            if (variant != null) {
                byProductId.merge(variant.getProductId(), sc.soldCount(), Integer::sum);
            }
        }
        return byProductId.entrySet().stream()
                .map(e -> new VariantProductSoldCountReconciliation(
                        e.getKey(),
                        e.getValue()))
                .toList();
    }

    private Map<VariantId, Variant> loadVariants(
            final Collection<VariantId> ids) {
        return this.loadAllPort.loadAllByIds(ids).stream()
                .collect(Collectors.toMap(Variant::getId, Function.identity()));
    }
}
