package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkHardDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkHardDeletionByProductIdForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountBulkDeletionByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkDeletionByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.service.command.image.VariantImageDeleter;
import vn.edu.uit.msshop.product.variant.domain.event.VariantHardDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
@Slf4j
class VariantBulkHardDeletionByProductIdForProductService
        implements VariantBulkHardDeletionByProductIdForProductUseCase {

    private final VariantBulkLookupByProductIdPort bulkLookupByProductIdPort;
    private final VariantBulkDeletionByProductIdPort bulkDeletionByProductIdPort;
    private final VariantSoldCountBulkDeletionByVariantIdsPort soldCountBulkDeletionByIdsPort;

    private final VariantImageDeleter imageDeleter;

    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    public void hardDeleteAll(
            final VariantBulkHardDeletionByProductIdForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());

        final var variants = this.bulkLookupByProductIdPort.loadAllByProductId(productId);
        if (variants.isEmpty()) {
            return;
        }

        final var variantIdList = variants.stream()
                .map(Variant::getId)
                .toList();

        this.bulkDeletionByProductIdPort.deleteByProductId(productId);
        this.soldCountBulkDeletionByIdsPort.deleteAllByVariantIds(variantIdList);

        for (final var variant : variants) {
            this.imageDeleter.deleteQuietly(variant.getImageKey());
        }

        for (final var variant : variants) {
            final var event = new VariantHardDeletedEvent(variant.getId());
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
