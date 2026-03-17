package vn.edu.uit.msshop.product.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.DeleteCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.DeleteCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteCategoryImageService implements DeleteCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;
    private final CategoryImageStoragePort imageStoragePort;
    private final CategoryViewMapper mapper;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public CategoryImageView deleteImage(
            final DeleteCategoryImageCommand command) {
        final var categoryId = command.id();
        final var category = this.loadPort.loadById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var oldKey = category.getImageKey();
        if (oldKey == null) {
            return this.mapper.toImageView(category);
        }

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = new Category(
                category.getId(),
                category.getName(),
                null,
                expectedVersion);
        final var saved = this.updatePort.update(next);

        final var event = new CategoryImageUpdated(
                saved.getId(),
                null,
                oldKey);
        this.eventPort.publish(event);

        this.deleteOldImage(oldKey);

        return this.mapper.toImageView(saved);
    }

    private void deleteOldImage(
            final CategoryImageKey oldKey) {
        try {
            this.imageStoragePort.deleteImage(oldKey);
        } catch (Exception e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
