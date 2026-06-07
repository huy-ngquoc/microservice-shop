package vn.edu.uit.msshop.product.category.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.CategoryLifecycleUseCases;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategoryRestoredEvent;
import vn.edu.uit.msshop.product.category.domain.model.Category;

@Service
@RequiredArgsConstructor
public class CategoryRestorationByIdService
        implements CategoryLifecycleUseCases.Restore {

    private final LoadSoftDeletedCategoryPort loadSoftDeletedPort;
    private final UpdateCategoryPort updatePort;

    private final CategoryEventPublicationPort eventPublicationPort;

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

        CategoryVersionGuard.ensureMatch(
                cmd.expectedVersion(),
                category.getVersion());

        final var next = new Category(
                category.getId(),
                category.getName(),
                category.getImageKey(),
                category.getVersion(),
                null);

        final var saved = this.updatePort.update(next);

        final var event = new CategoryRestoredEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);
    }
}
