package vn.edu.uit.msshop.product.category.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.HardDeleteCategoryCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.HardDeleteCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.image.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.DeleteCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasProductsPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryPurged;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.product.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class HardDeleteCategoryService implements HardDeleteCategoryUseCase {
    private final LoadSoftDeletedCategoryPort loadSoftDeletedPort;
    private final CheckCategoryHasProductsPort checkHasProductsPort;
    private final DeleteCategoryPort deletePort;
    private final CategoryImageStoragePort imageStoragePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void purge(
            final HardDeleteCategoryCommand command) {
        final var categoryId = command.id();
        final var category = this.loadSoftDeletedPort.loadSoftDeletedById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        if (this.checkHasProductsPort.hasProduct(categoryId)) {
            throw new BusinessRuleException(
                    "Cannot delete category with existing products");
        }

        this.deletePort.deleteById(categoryId);
        this.eventPort.publish(new CategoryPurged(categoryId));

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
            log.warn("Hard delete: failed to delete image '{}', manual cleanup required", key.value(), e);
        }
    }
}
