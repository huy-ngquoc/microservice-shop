package vn.edu.uit.msshop.product.variant.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.variant.application.dto.command.HardDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.HardDeleteVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadSoftDeletedVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckVariantReferencedByProductPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantPurged;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class HardDeleteVariantService implements HardDeleteVariantUseCase {
    private final LoadSoftDeletedVariantPort loadSoftDeletedPort;
    private final DeleteVariantPort deletePort;
    private final DeleteVariantSoldCountPort deleteSoldCountPort;
    private final CheckVariantReferencedByProductPort checkReferencedPort;
    private final VariantImageStoragePort imageStoragePort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void purge(
            final HardDeleteVariantCommand command) {
        final var variantId = command.id();
        final var variant = this.loadSoftDeletedPort
                .loadSoftDeletedById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var referenced = this.checkReferencedPort
                .isReferencedByProduct(variantId);
        if (referenced) {
            throw new BusinessRuleException(
                    "Cannot purge variant: still referenced by a product");
        }

        this.deletePort.deleteById(variantId);
        this.deleteSoldCountPort.deleteById(variantId);

        this.eventPort.publish(new VariantPurged(variantId));

        this.deleteImage(variant.getImageKey());
    }

    private void deleteImage(
            @Nullable final VariantImageKey key) {
        if (key == null) {
            return;
        }

        try {
            this.imageStoragePort.deleteImage(key);
        } catch (final RuntimeException e) {
            log.warn("Failed to delete image '{}', manual cleanup required", key.value(), e);
        }
    }
}
