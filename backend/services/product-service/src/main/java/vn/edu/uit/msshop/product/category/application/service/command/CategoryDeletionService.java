package vn.edu.uit.msshop.product.category.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.CategoryLifecycleUseCases;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.image.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.DeleteCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasProductsPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryHardDeletedEvent;
import vn.edu.uit.msshop.product.category.domain.event.CategoryRestoredEvent;
import vn.edu.uit.msshop.product.category.domain.event.CategorySoftDeletedEvent;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryDeletionTime;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryDeletionService
        implements
        CategoryLifecycleUseCases.SoftDelete,
        CategoryLifecycleUseCases.Restore,
        CategoryLifecycleUseCases.HardDelete {

    private final LoadCategoryPort loadPort;
    private final LoadSoftDeletedCategoryPort loadSoftDeletedPort;
    private final UpdateCategoryPort updatePort;
    private final DeleteCategoryPort deletePort;

    private final CheckCategoryHasProductsPort checkHasProductsPort;
    private final CheckCategoryHasSoftDeletedProductsPort checkHasSoftDeletedProductsPort;

    private final CategoryImageStoragePort imageStoragePort;

    private final CategoryEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY,
                            key = "#cmd.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY_LIST,
                            allEntries = true)
            })
    public void softDelete(
            final CategoryLifecycleCommands.SoftDelete cmd) {
        final var categoryId = cmd.id();
        final var category = this.loadPort.loadById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var expectedVersion = cmd.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(expectedVersion.value(),
                    currentVersion.value());
        }

        if (this.checkHasProductsPort.hasProduct(categoryId)) {
            throw new BusinessRuleException(
                    "Cannot delete category with existing products");
        }

        final var next = new Category(
                category.getId(),
                category.getName(),
                category.getImageKey(),
                category.getVersion(),
                CategoryDeletionTime.now());

        final var saved = this.updatePort.update(next);
        this.eventPublicationPort.publishEvent(new CategorySoftDeletedEvent(saved.getId()));
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CATEGORY_LIST,
            allEntries = true)
    public void restore(
            final CategoryLifecycleCommands.Restore cmd) {
        final var categoryId = cmd.id();
        final var category = this.loadSoftDeletedPort.loadSoftDeletedById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var expectedVersion = cmd.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = new Category(
                category.getId(),
                category.getName(),
                category.getImageKey(),
                category.getVersion(),
                null);

        final var saved = this.updatePort.update(next);
        this.eventPublicationPort.publishEvent(new CategoryRestoredEvent(saved.getId()));
    }

    @Override
    @Transactional
    public void hardDelete(
            final CategoryLifecycleCommands.HardDelete cmd) {
        final var categoryId = cmd.id();
        final var category = this.loadSoftDeletedPort.loadSoftDeletedById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var expectedVersion = cmd.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        if (this.checkHasSoftDeletedProductsPort.hasSoftDeletedProduct(categoryId)) {
            throw new BusinessRuleException("Cannot delete category with existing products");
        }

        this.deletePort.deleteById(categoryId);
        this.eventPublicationPort.publishEvent(new CategoryHardDeletedEvent(categoryId));

        this.deleteLogo(category.getImageKey());
    }

    private void deleteLogo(
            @Nullable
            final CategoryImageKey key) {
        if (key == null) {
            return;
        }

        try {
            this.imageStoragePort.deleteImage(key);
        } catch (final RuntimeException e) {
            log.warn("Hard delete: failed to delete image '{}', manual cleanup required",
                    key.value(),
                    e);
        }
    }
}
