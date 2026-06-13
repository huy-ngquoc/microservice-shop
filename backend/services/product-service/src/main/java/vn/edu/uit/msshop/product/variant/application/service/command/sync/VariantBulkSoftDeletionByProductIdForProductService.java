package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByProductIdForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

// TODO: should delete image?
@Service
@RequiredArgsConstructor
class VariantBulkSoftDeletionByProductIdForProductService
        implements VariantBulkSoftDeletionByProductIdForProductUseCase {

    private final LoadVariantsForProductPort loadForProductPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final VariantEventPublicationPort eventPublicationPort;

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
    public void deleteByProductId(
            final VariantBulkSoftDeletionByProductIdForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());

        final var variants = this.loadForProductPort.loadAllByProductId(productId);
        if (variants.isEmpty()) {
            return;
        }

        final var next = variants.stream()
                .map(VariantBulkSoftDeletionByProductIdForProductService::toSoftDeleted)
                .toList();

        final var saved = this.updateAllPort.updateAll(next);
        for (final var variant : saved) {
            final var event = new VariantSoftDeletedEvent(variant.getId());
            this.eventPublicationPort.publishEvent(event);
        }
    }

    private static Variant toSoftDeleted(
            final Variant variant) {
        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());
    }
}
