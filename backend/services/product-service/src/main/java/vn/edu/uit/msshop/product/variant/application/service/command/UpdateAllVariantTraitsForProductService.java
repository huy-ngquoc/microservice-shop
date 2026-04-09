package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateAllVariantTraitsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Service
@RequiredArgsConstructor
public class UpdateAllVariantTraitsForProductService
        implements UpdateAllVariantTraitsForProductUseCase {
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void updateTraitsByIds(
            Map<VariantId, VariantTraits> newTraitsMap) {
        final var ids = newTraitsMap.keySet();
        final var variants = this.loadAllPort.loadAllByIds(ids);

        final var next = variants.stream()
                .map(v -> withNewTraits(v, newTraitsMap.get(v.getId())))
                .toList();

        final var saved = this.updateAllPort.updateAll(next);
        for (final var v : saved) {
            this.eventPort.publish(new VariantUpdated(v.getId()));
        }
    }

    private static Variant withNewTraits(
            final Variant variant,
            @Nullable
            final VariantTraits newTraits) {
        if (newTraits == null) {
            throw new BusinessRuleException("Missing traits for variant: " + variant.getId().value());
        }

        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getSoldCount(),
                newTraits,
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                variant.getDeletionTime());
    }
}
