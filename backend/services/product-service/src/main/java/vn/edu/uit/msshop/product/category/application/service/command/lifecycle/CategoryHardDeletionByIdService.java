package vn.edu.uit.msshop.product.category.application.service.command.lifecycle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle.CategoryHardDeletionByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command.CategoryDeletionByIdPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup.CategorySoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CategoryProductSoftDeletedExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.category.application.service.command.image.CategoryImageDeleter;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategoryHardDeletedEvent;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryHardDeletionByIdService
        implements CategoryHardDeletionByIdUseCase {

    private final CategorySoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final CategoryDeletionByIdPort deletionByIdPort;

    private final CategoryProductSoftDeletedExistenceCheckByCategoryIdPort productSoftDeletedExistenceCheckByCategoryIdPort;

    private final CategoryImageDeleter imageDeleter;

    private final CategoryEventPublicationPort eventPort;

    @Override
    @Transactional
    public void hardDelete(
            final CategoryHardDeletionByIdCommand cmd) {
        final var categoryId = new CategoryId(cmd.categoryId());
        final var expectedVersion = new CategoryVersion(cmd.categoryVersion());

        final var category = this.softDeletedLookupByIdPort.loadSoftDeletedById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        CategoryVersionGuard.ensureMatch(
                expectedVersion,
                category.getVersion());

        if (this.productSoftDeletedExistenceCheckByCategoryIdPort
                .existsSoftDeletedByCategoryId(categoryId)) {
            throw new BusinessRuleException("Cannot delete category with existing products");
        }

        this.deletionByIdPort.deleteById(categoryId);

        final var event = new CategoryHardDeletedEvent(categoryId);
        this.eventPort.publishEvent(event);

        this.imageDeleter.deleteQuietly(category.getImageKey());
    }
}
