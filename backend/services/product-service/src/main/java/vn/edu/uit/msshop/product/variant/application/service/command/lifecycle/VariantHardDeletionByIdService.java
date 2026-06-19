package vn.edu.uit.msshop.product.variant.application.service.command.lifecycle;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantHardDeletionByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantSoldCountDeletionByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command.VariantStockCountDeletionByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantDeletionByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.VariantReferencedByProductCheckPort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantVersionGuard;
import vn.edu.uit.msshop.product.variant.domain.event.VariantHardDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

@Service
@RequiredArgsConstructor
@Slf4j
class VariantHardDeletionByIdService
        implements VariantHardDeletionByIdUseCase {

    private final VariantSoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final VariantDeletionByIdPort deletionByIdPort;
    private final VariantSoldCountDeletionByVariantIdPort soldCountDeletionByIdPort;
    private final VariantStockCountDeletionByVariantIdPort stockCountDeletionByIdPort;
    private final VariantReferencedByProductCheckPort checkReferencedPort;
    private final VariantImageStoragePort imageStoragePort;
    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    public void hardDelete(
            final VariantHardDeletionByIdCommand cmd) {
        final var variantId = new VariantId(cmd.variantId());
        final var expectedVersion = new VariantVersion(cmd.variantVersion());

        final var variant = this.softDeletedLookupByIdPort.loadSoftDeletedById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        VariantVersionGuard.ensureMatch(
                expectedVersion,
                variant.getVersion());

        final var referenced = this.checkReferencedPort.isReferencedByProduct(variantId);
        if (referenced) {
            throw new BusinessRuleException("Cannot purge variant: still referenced by a product");
        }

        this.deletionByIdPort.deleteById(variantId);
        this.soldCountDeletionByIdPort.deleteByVariantId(variantId);
        this.stockCountDeletionByIdPort.deleteByVariantId(variantId);

        final var event = VariantHardDeletedEvent.of(variant);
        this.eventPublicationPort.publishEvent(event);

        this.deleteImage(variant.getImageKey());
    }

    private void deleteImage(
            @Nullable
            final VariantImageKey key) {
        if (key == null) {
            return;
        }

        try {
            this.imageStoragePort.deleteImage(key);
        } catch (final RuntimeException e) {
            log.warn("Failed to delete image '{}', manual cleanup required",
                    key.value(),
                    e);
        }
    }
}
