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
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

// TODO: should delete image?
@Service
@RequiredArgsConstructor
class VariantBulkSoftDeletionByProductIdForProductService
        implements VariantBulkSoftDeletionByProductIdForProductUseCase {

    private final VariantBulkLookupByProductIdPort bulkLookupByProductIdPort;
    private final VariantBulkUpdatePort bulkUpdatePort;
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
    public void softDeleteAll(
            final VariantBulkSoftDeletionByProductIdForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());

        final var variants = this.bulkLookupByProductIdPort.loadAllByProductId(productId);
        if (variants.isEmpty()) {
            return;
        }

        final var next = variants.stream()
                .map(Variant::softDeleted)
                .toList();

        final var saved = this.bulkUpdatePort.updateAll(next);
        for (final var variant : saved) {
            final var event = new VariantSoftDeletedEvent(variant.getId());
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
