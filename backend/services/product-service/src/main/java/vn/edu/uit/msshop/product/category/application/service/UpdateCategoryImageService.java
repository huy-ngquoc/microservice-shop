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
import vn.edu.uit.msshop.product.category.application.port.out.MoveCategoryImagePort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.VerifyCategoryImageKeyPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCategoryImageService implements UpdateCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final SaveCategoryPort savePort;
    private final VerifyCategoryImageKeyPort verifyImageKeyPort;
    private final MoveCategoryImagePort moveImagePort;
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

        final var next = this.applyChanges(category, imageKeySet);
        if (next == null) {
            return;
        }

        this.moveImagePort.moveToCategory(imageKeySet.value());
        final var saved = this.saveWithCompensation(next, imageKeySet.value());

        final var event = new CategoryImageUpdated(
                saved.getId(),
                saved.getImageKey(),
                category.getImageKey());
        this.eventPort.publish(event);

        this.deleteOldImage(category.getImageKey());
    }

    private void ensureImageKeyExistsInTemp(
            final CategoryImageKey imageKey) {
        if (!this.verifyImageKeyPort.existsInTemp(imageKey)) {
            throw new CategoryImageKeyNotFoundException(imageKey);
        }
    }

    private @Nullable Category applyChanges(
            final Category current,
            final Change.Set<CategoryImageKey> imageKeySet) {
        if (imageKeySet.value().equals(current.getImageKey())) {
            return null;
        }

        return new Category(
                current.getId(),
                current.getName(),
                imageKeySet.value());
    }

    private Category saveWithCompensation(
            final Category next,
            final CategoryImageKey newKey) {
        try {
            return this.savePort.save(next);
        } catch (final RuntimeException e) {
            try {
                this.moveImagePort.moveBackToTemp(newKey);
            } catch (final RuntimeException compensateEx) {
                e.addSuppressed(compensateEx);
                log.error("Compensation failed for key '{}'", newKey.value(), compensateEx);
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
            this.moveImagePort.deleteFromCategory(oldKey);
        } catch (Exception e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
