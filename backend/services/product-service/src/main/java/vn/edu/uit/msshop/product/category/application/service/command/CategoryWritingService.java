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
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.command.CategoryLifecycleUseCases;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.CreateCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreatedEvent;
import vn.edu.uit.msshop.product.category.domain.event.CategoryInfoUpdatedEvent;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.creation.NewCategory;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;
import vn.edu.uit.msshop.shared.application.dto.Change;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryWritingService
        implements
        CategoryLifecycleUseCases.Create,
        CategoryLifecycleUseCases.UpdateInfo {

    private final LoadCategoryPort loadPort;
    private final CreateCategoryPort createPort;
    private final UpdateCategoryPort updatePort;

    private final CategoryEventPublicationPort eventPublicationPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CATEGORY_LIST,
            allEntries = true)
    public CategoryView create(
            final CategoryLifecycleCommands.Create cmd) {
        final var newCategory = new NewCategory(
                CategoryId.newId(),
                cmd.name());

        final var saved = this.createPort.create(newCategory);
        this.eventPublicationPort.publishEvent(new CategoryCreatedEvent(saved.getId()));

        return this.mapper.toView(saved);
    }

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

        final var expectedVersion = cmd.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = CategoryWritingService.applyChanges(category, nameSet);
        if (next == null) {
            return this.mapper.toView(category);
        }

        final var saved = this.updatePort.update(next);
        this.eventPublicationPort.publishEvent(new CategoryInfoUpdatedEvent(saved.getId()));

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

        return new Category(
                current.getId(),
                applyNameResult.newValue(),
                current.getImageKey(),
                current.getVersion(),
                current.getDeletionTime());
    }
}
