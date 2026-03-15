package vn.edu.uit.msshop.product.category.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryImageKeyNotFoundException;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryVersion;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCategoryImageService implements UpdateCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final SaveCategoryPort savePort;
    private final CategoryImageStoragePort imageStoragePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void updateImage(
            final UpdateCategoryImageCommand command) {
        final var imageKeySet = command.imageKey().getSet();
        if (imageKeySet == null) {
            return;
        }

        this.ensureImageKeyExistsInTemp(imageKeySet.value());

        final var category = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new CategoryNotFoundException(command.id()));

        final var next = this.applyChanges(category, imageKeySet, command.expectedVersion());
        if (next == null) {
            return;
        }

        final var saved = this.publishImageAndSave(next);

        final var event = new CategoryImageUpdated(
                saved.getId(),
                saved.getImageKey(),
                category.getImageKey());
        this.eventPort.publish(event);

        this.deleteOldImage(category.getImageKey());
    }

    private void ensureImageKeyExistsInTemp(
            final CategoryImageKey imageKey) {
        if (!this.imageStoragePort.existsAsTemp(imageKey)) {
            throw new CategoryImageKeyNotFoundException(imageKey);
        }
    }

    private @Nullable Category applyChanges(
            final Category current,
            final Change.Set<CategoryImageKey> imageKeySet,
            final CategoryVersion expectedVersion) {
        if (imageKeySet.value().equals(current.getImageKey())) {
            return null;
        }

        return new Category(
                current.getId(),
                current.getName(),
                imageKeySet.value(),
                expectedVersion);
    }

    private Category publishImageAndSave(
            final Category next) {
        final var imageKey = next.getImageKey();
        this.imageStoragePort.publishImage(imageKey);

        try {
            return this.savePort.save(next);
        } catch (final RuntimeException e) {
            try {
                this.imageStoragePort.unpublishImage(imageKey);
            } catch (final RuntimeException compensateEx) {
                e.addSuppressed(compensateEx);
                log.error("Compensation failed for key '{}'", imageKey.value(), compensateEx);
            }
            throw e;
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
        } catch (Exception e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
