package vn.edu.uit.msshop.product.category.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.command.SoftDeleteCategoryCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.SoftDeleteCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasProductsPort;
import vn.edu.uit.msshop.product.category.domain.event.CategorySoftDeleted;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryDeletionTime;
import vn.edu.uit.msshop.product.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class SoftDeleteCategoryService implements SoftDeleteCategoryUseCase {
    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;
    private final CheckCategoryHasProductsPort checkHasProductsPort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void delete(
            final SoftDeleteCategoryCommand command) {
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
        this.eventPort.publish(new CategorySoftDeleted(saved.getId()));
    }
}
