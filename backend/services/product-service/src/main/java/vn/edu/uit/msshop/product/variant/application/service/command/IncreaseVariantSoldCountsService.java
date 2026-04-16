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
import vn.edu.uit.msshop.product.shared.application.port.event.EventIdempotencyPort;
import vn.edu.uit.msshop.product.variant.application.dto.command.IncreaseVariantSoldCountsCommand;
import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantProductSoldCountIncrement;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.IncreaseVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.IncreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
public class IncreaseVariantSoldCountsService
        implements IncreaseVariantSoldCountsUseCase {
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final IncreaseProductSoldCountsPort increaseProductSoldCountsPort;
    private final EventIdempotencyPort eventIdempotencyPort;

    @Override
    @Transactional
    public void execute(
            final IncreaseVariantSoldCountsCommand command) {
        if (!this.eventIdempotencyPort.tryAcquire(command.eventId())) {
            return;
        }

        final var incrementByVariantId = IncreaseVariantSoldCountsService
                .aggregateByVariantId(command);
        final var loadedById = this.loadVariants(incrementByVariantId.keySet());

        final var updated = IncreaseVariantSoldCountsService.applyIncrements(
                loadedById,
                incrementByVariantId);
        this.updateAllPort.updateAll(updated);

        final var productItems = IncreaseVariantSoldCountsService
                .toProductIncrements(updated, incrementByVariantId);
        this.increaseProductSoldCountsPort.increaseSoldCounts(productItems);
    }

    private static Map<VariantId, Integer> aggregateByVariantId(
            final IncreaseVariantSoldCountsCommand command) {
        return command.items().stream()
                .collect(Collectors.toMap(
                        IncreaseVariantSoldCountsCommand.Item::variantId,
                        IncreaseVariantSoldCountsCommand.Item::soldCountIncrement,
                        Integer::sum));
    }

    private static List<Variant> applyIncrements(
            final Map<VariantId, Variant> loadedById,
            final Map<VariantId, Integer> incrementByVariantId) {
        return incrementByVariantId.entrySet().stream()
                .map(e -> IncreaseVariantSoldCountsService.applyIncrement(
                        loadedById,
                        e.getKey(),
                        e.getValue()))
                .toList();
    }

    private static Variant applyIncrement(
            final Map<VariantId, Variant> loadedById,
            final VariantId variantId,
            final int increment) {
        final var variant = loadedById.get(variantId);
        if (variant == null) {
            throw new VariantNotFoundException(variantId);
        }
        return IncreaseVariantSoldCountsService.withIncreasedSoldCount(variant, increment);
    }

    private static List<VariantProductSoldCountIncrement> toProductIncrements(
            final Collection<Variant> variants,
            final Map<VariantId, Integer> incrementByVariantId) {
        final var byProductId = new HashMap<VariantProductId, Integer>();
        for (final var variant : variants) {
            byProductId.merge(variant.getProductId(),
                    incrementByVariantId.get(variant.getId()),
                    Integer::sum);
        }
        return byProductId.entrySet().stream()
                .map(e -> new VariantProductSoldCountIncrement(
                        e.getKey(),
                        e.getValue()))
                .toList();
    }

    private static Variant withIncreasedSoldCount(
            final Variant current,
            final int increment) {
        final var newSoldCount = current.getSoldCount().increase(increment);
        return new Variant(
                current.getId(),
                current.getProductId(),
                current.getPrice(),
                newSoldCount,
                current.getTraits(),
                current.getTargets(),
                current.getImageKey(),
                current.getVersion(),
                current.getDeletionTime());
    }

    private Map<VariantId, Variant> loadVariants(
            final Collection<VariantId> ids) {
        return this.loadAllPort.loadAllByIds(ids).stream()
                .collect(Collectors.toMap(Variant::getId, Function.identity()));
    }

}
