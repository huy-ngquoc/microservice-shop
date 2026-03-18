package vn.edu.uit.msshop.product.category.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryImageKeyNotFoundException;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCategoryImageService implements UpdateCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;
    private final CategoryImageStoragePort imageStoragePort;
    private final CategoryViewMapper mapper;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public CategoryImageView updateImage(
            final UpdateCategoryImageCommand command) {
        final var categoryId = command.id();
        final var category = this.loadPort.loadById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var saved = this.commitImageChange(category, command.newImageKey());
        if (saved == null) {
            return this.mapper.toImageView(category);
        }

        final var event = new CategoryImageUpdated(
                saved.getId(),
                saved.getImageKey(),
                category.getImageKey());
        this.eventPort.publish(event);

        this.deleteOldImage(category.getImageKey());

        return this.mapper.toImageView(saved);
    }

    private @Nullable Category commitImageChange(
            final Category current,
            final CategoryImageKey newImageKey) {
        if (newImageKey.equals(current.getImageKey())) {
            return null;
        }

        this.ensureImageKeyExistsInTemp(newImageKey);
        this.imageStoragePort.publishImage(newImageKey);

        try {
            final var next = new Category(
                    current.getId(),
                    current.getName(),
                    newImageKey,
                    current.getVersion());
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
            final CategoryImageKey imageKey) {
        if (!this.imageStoragePort.existsAsTemp(imageKey)) {
            throw new CategoryImageKeyNotFoundException(imageKey);
        }
    }

    private void deleteOldImage(
            @Nullable
            final CategoryImageKey oldKey) {
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
