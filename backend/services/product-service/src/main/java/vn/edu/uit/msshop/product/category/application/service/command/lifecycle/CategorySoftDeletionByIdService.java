package vn.edu.uit.msshop.product.category.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategorySoftDeletionByIdCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle.CategorySoftDeletionByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasProductsPort;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategorySoftDeletedEvent;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
public class CategorySoftDeletionByIdService
        implements CategorySoftDeletionByIdUseCase {

    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;

    private final CheckCategoryHasProductsPort checkHasProductsPort;

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
            final CategorySoftDeletionByIdCommand cmd) {
        final var categoryId = new CategoryId(cmd.categoryId());
        final var expectedVersion = new CategoryVersion(cmd.categoryVersion());

        final var category = this.loadPort.loadById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        CategoryVersionGuard.ensureMatch(
                expectedVersion,
                category.getVersion());

        if (this.checkHasProductsPort.hasProduct(categoryId)) {
            throw new BusinessRuleException(
                    "Cannot delete category with existing products");
        }

        final var next = category.softDeleted();
        final var saved = this.updatePort.update(next);

        final var event = new CategorySoftDeletedEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);
    }
}
