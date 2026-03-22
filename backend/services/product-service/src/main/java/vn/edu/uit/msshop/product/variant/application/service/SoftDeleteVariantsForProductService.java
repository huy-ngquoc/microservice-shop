package vn.edu.uit.msshop.product.variant.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.exception.VariantProductNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.SoftDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

// TODO: should delete image?
@Service
@RequiredArgsConstructor
public class SoftDeleteVariantsForProductService implements SoftDeleteVariantsForProductUseCase {
    private final LoadVariantsForProductPort loadForProductPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final PublishVariantEventPort eventPort;

    public void deleteByProductId(
            final VariantProductId productId) {
        final var variants = this.loadForProductPort.loadByProductId(productId);
        if (variants.isEmpty()) {
            throw new VariantProductNotFoundException(productId);
        }

        final var next = variants.stream()
                .map(this::toSoftDeleted).toList();

        final var saved = this.updateAllPort.updateAll(next);

        saved.stream().forEach(v -> this.eventPort.publish(new VariantSoftDeleted(v.getId())));
    }

    private Variant toSoftDeleted(
            final Variant variant) {
        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getSoldCount(),
                variant.getTraits(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());
    }
}
