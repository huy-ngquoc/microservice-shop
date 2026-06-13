package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkHardDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkHardDeletionByProductIdForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.service.command.image.VariantImageDeleter;
import vn.edu.uit.msshop.product.variant.domain.event.VariantHardDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
@Slf4j
class VariantBulkHardDeletionByProductIdForProductService
        implements VariantBulkHardDeletionByProductIdForProductUseCase {

    private final LoadVariantsForProductPort loadForProductPort;
    private final DeleteVariantsForProductPort deleteForProductPort;
    private final DeleteAllVariantSoldCountsPort deleteAllSoldCountsPort;

    private final VariantImageDeleter imageDeleter;

    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    public void purgeByProductId(
            final VariantBulkHardDeletionByProductIdForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());

        final var variants = this.loadForProductPort.loadAllByProductId(productId);
        if (variants.isEmpty()) {
            return;
        }

        final var variantIdList = variants.stream()
                .map(Variant::getId)
                .toList();

        this.deleteForProductPort.deleteByProductId(productId);
        this.deleteAllSoldCountsPort.deleteAllByIds(variantIdList);

        for (final var variant : variants) {
            this.imageDeleter.deleteQuietly(variant.getImageKey());
        }

        for (final var variant : variants) {
            final var event = new VariantHardDeletedEvent(variant.getId());
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
