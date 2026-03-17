package vn.edu.uit.msshop.product.variant.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.domain.event.VariantImageUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.DeleteVariantImageUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.VariantImageStoragePort;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteVariantImageService implements DeleteVariantImageUseCase {
    private final LoadVariantPort loadPort;
    private final UpdateVariantPort updatePort;
    private final VariantImageStoragePort imageStoragePort;
    private final VariantViewMapper mapper;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public VariantImageView deleteImage(
            final DeleteVariantImageCommand command) {
        final var variantId = command.id();
        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var oldKey = variant.getImageKey();
        if (oldKey == null) {
            return this.mapper.toImageView(variant);
        }

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getSoldCount(),
                variant.getTraits(),
                null,
                expectedVersion);
        final var saved = this.updatePort.update(next);

        final var event = new VariantImageUpdated(
                saved.getId(),
                null,
                oldKey);
        this.eventPort.publish(event);

        this.deleteOldImage(oldKey);

        return this.mapper.toImageView(saved);
    }

    private void deleteOldImage(
            final VariantImageKey oldKey) {
        try {
            this.imageStoragePort.deleteImage(oldKey);
        } catch (Exception e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
