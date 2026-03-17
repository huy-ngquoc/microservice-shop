package vn.edu.uit.msshop.product.variant.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantImageKeyNotFoundException;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.out.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantImageUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.VariantVersion;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.variant.application.port.in.UpdateVariantImageUseCase;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateVariantImageService implements UpdateVariantImageUseCase {
    private final LoadVariantPort loadPort;
    private final UpdateVariantPort updatePort;
    private final VariantImageStoragePort imageStoragePort;
    private final VariantViewMapper mapper;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public VariantImageView updateImage(
            final UpdateVariantImageCommand command) {
        final var variantId = command.id();
        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var saved = this.commitImageChange(variant, command.newImageKey(), expectedVersion);
        if (saved == null) {
            return this.mapper.toImageView(variant);
        }

        final var event = new VariantImageUpdated(
                saved.getId(),
                saved.getImageKey(),
                variant.getImageKey());
        this.eventPort.publish(event);

        this.deleteOldImage(variant.getImageKey());

        return this.mapper.toImageView(saved);
    }

    private @Nullable Variant commitImageChange(
            final Variant current,
            final VariantImageKey newImageKey,
            final VariantVersion expectedVersion) {
        if (newImageKey.equals(current.getImageKey())) {
            return null;
        }

        this.ensureImageKeyExistsInTemp(newImageKey);
        this.imageStoragePort.publishImage(newImageKey);

        try {
            final var next = new Variant(
                    current.getId(),
                    current.getProductId(),
                    current.getPrice(),
                    current.getSoldCount(),
                    current.getTraits(),
                    newImageKey,
                    expectedVersion);
            return this.updatePort.update(next);
        } catch (final RuntimeException e) {
            try {
                this.imageStoragePort.unpublishImage(newImageKey);
            } catch (final RuntimeException compensateEx) {
                e.addSuppressed(compensateEx);
                log.error("Compensation failed for key '{}'", newImageKey.value(), compensateEx);
            }
            throw e;
        }
    }

    private void ensureImageKeyExistsInTemp(
            final VariantImageKey imageKey) {
        if (!this.imageStoragePort.existsAsTemp(imageKey)) {
            throw new VariantImageKeyNotFoundException(imageKey);
        }
    }

    private void deleteOldImage(
            @Nullable
            final VariantImageKey oldKey) {
        if (oldKey == null) {
            return;
        }

        try {
            this.imageStoragePort.deleteImage(oldKey);
        } catch (final Exception e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
