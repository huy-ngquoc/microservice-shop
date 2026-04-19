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
import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantProductSoldCountIncrement;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCount;

@Service
@RequiredArgsConstructor
public class SetVariantSoldCountsService
        implements SetVariantSoldCountsUseCase {
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final IncreaseProductSoldCountsPort increaseProductSoldCountsPort;

    @Override
    @Transactional
    public void execute(
            final Collection<VariantOrderSoldCount> orderSoldCounts) {
        if (orderSoldCounts.isEmpty()) {
            return;
        }

        final var resolved = this.resolve(orderSoldCounts);

        final var updated = resolved.stream()
                .map(ResolvedItem::updated)
                .toList();
        this.updateAllPort.updateAll(updated);

        final var productIncrements = SetVariantSoldCountsService.toProductIncrements(resolved);
        if (!productIncrements.isEmpty()) {
            this.increaseProductSoldCountsPort.increaseSoldCounts(productIncrements);
        }
    }

    private static ResolvedItem resolve(
            final VariantOrderSoldCount sc,
            final Map<VariantId, Variant> loadedById) {
        final var variant = loadedById.get(sc.variantId());
        if (variant == null) {
            throw new VariantNotFoundException(sc.variantId());
        }
        return new ResolvedItem(variant, sc);
    }

    private static List<VariantProductSoldCountIncrement> toProductIncrements(
            final List<ResolvedItem> resolved) {
        final var byProductId = HashMap.<VariantProductId, Integer>newHashMap(resolved.size());
        for (final var item : resolved) {
            final var delta = item.delta();
            if (delta > 0) {
                byProductId.merge(item.current().getProductId(), delta, Integer::sum);
            }
        }
        return byProductId.entrySet().stream()
                .map(e -> new VariantProductSoldCountIncrement(e.getKey(), e.getValue()))
                .toList();
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

    private List<ResolvedItem> resolve(
            final Collection<VariantOrderSoldCount> orderSoldCounts) {
        final var variantIds = orderSoldCounts.stream()
                .map(VariantOrderSoldCount::variantId)
                .toList();
        final var loadedById = this.loadVariants(variantIds);

        return orderSoldCounts.stream()
                .map(sc -> SetVariantSoldCountsService.resolve(sc, loadedById))
                .toList();
    }

    private Map<VariantId, Variant> loadVariants(
            final Collection<VariantId> ids) {
        return this.loadAllPort.loadAllByIds(ids).stream()
                .collect(Collectors.toMap(Variant::getId, Function.identity()));
    }

    private record ResolvedItem(
            Variant current,
            VariantOrderSoldCount orderSoldCount) {

        Variant updated() {
            return SetVariantSoldCountsService.withNewSoldCount(
                    this.current, this.orderSoldCount.soldCount());
        }

        int delta() {
            return this.orderSoldCount.soldCount() - this.current.getSoldCount().value();
        }
    }
}
