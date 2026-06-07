package vn.edu.uit.msshop.product.category.application.service.command.lifecycle;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.command.CategoryLifecycleUseCases;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategoryInfoUpdatedEvent;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;
import vn.edu.uit.msshop.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryInfoUpdateByIdService
        implements CategoryLifecycleUseCases.UpdateInfo {

    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;

    private final CategoryEventPublicationPort eventPublicationPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY,
                            key = "#cmd.id().value()",
                            condition = "#cmd.name().getSet() != null"),
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY_LIST,
                            allEntries = true,
                            condition = "#cmd.name().getSet() != null")
            })
    public CategoryView updateInfo(
            final CategoryLifecycleCommands.UpdateInfo cmd) {
        final var categoryId = cmd.id();
        final var category = this.loadPort.loadById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var nameSet = cmd.name().getSet();
        if (nameSet == null) {
            return this.mapper.toView(category);
        }

        CategoryVersionGuard.ensureMatch(
                cmd.expectedVersion(),
                category.getVersion());

        final var next = CategoryInfoUpdateByIdService.applyChanges(category, nameSet);
        if (next == null) {
            return this.mapper.toView(category);
        }

        final var saved = this.updatePort.update(next);

        final var event = new CategoryInfoUpdatedEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);

        return this.mapper.toView(saved);
    }

    private static @Nullable Category applyChanges(
            final Category current,
            final Change.Set<CategoryName> nameSet) {
        final var applyNameResult = Change.Set.applyChange(
                nameSet,
                current.getName());
        if (!applyNameResult.changed()) {
            return null;
        }

        final var newName = applyNameResult.newValue();
        return current.rename(newName);
    }
}
