package vn.edu.uit.msshop.product.category.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryRestorationByIdCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle.CategoryRestorationByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command.CategoryUpdatePort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup.CategorySoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategoryRestoredEvent;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;

@Service
@RequiredArgsConstructor
class CategoryRestorationByIdService
        implements CategoryRestorationByIdUseCase {

    private final CategorySoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final CategoryUpdatePort updatePort;

    private final CategoryEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CATEGORY_LIST,
            allEntries = true)
    public void restore(
            final CategoryRestorationByIdCommand cmd) {
        final var categoryId = new CategoryId(cmd.categoryId());
        final var expectedVersion = new CategoryVersion(cmd.categoryVersion());

        final var category = this.softDeletedLookupByIdPort.loadSoftDeletedById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        CategoryVersionGuard.ensureMatch(
                expectedVersion,
                category.getVersion());

        final var next = category.restored();
        final var saved = this.updatePort.update(next);

        final var event = new CategoryRestoredEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);
    }
}
