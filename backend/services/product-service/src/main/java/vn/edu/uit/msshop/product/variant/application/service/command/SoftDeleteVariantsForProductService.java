package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.exception.VariantProductNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

// TODO: should delete image?
@Service
@RequiredArgsConstructor
public class SoftDeleteVariantsForProductService implements SoftDeleteVariantsForProductUseCase {
    private final LoadVariantsForProductPort loadForProductPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void deleteByProductId(
            final VariantProductId productId) {
        final var variants = this.loadForProductPort.loadAllByProductId(productId);
        if (variants.isEmpty()) {
            throw new VariantProductNotFoundException(productId);
        }

        final var next = variants.stream()
                .map(this::toSoftDeleted).toList();

        final var saved = this.updateAllPort.updateAll(next);

        saved.forEach(s -> this.eventPort.publish(new VariantSoftDeleted(s.getId())));
    }

    private Variant toSoftDeleted(
            final Variant variant) {
        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getSoldCount(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());
    }
}
