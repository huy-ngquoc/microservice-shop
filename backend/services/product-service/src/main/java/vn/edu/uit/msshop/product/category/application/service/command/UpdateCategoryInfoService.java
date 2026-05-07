package vn.edu.uit.msshop.product.category.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.command.UpdateCategoryInfoUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryUpdated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;
import vn.edu.uit.msshop.shared.application.dto.Change;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class UpdateCategoryInfoService implements UpdateCategoryInfoUseCase {
    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;
    private final CategoryViewMapper mapper;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY,
                            key = "#command.id().value()",
                            condition = "#command.name().getSet() != null"),
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY_LIST,
                            allEntries = true,
                            condition = "#command.name().getSet() != null")
            })
    public CategoryView updateInfo(
            final UpdateCategoryInfoCommand command) {
        final var categoryId = command.id();
        final var category = this.loadPort.loadById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var nameSet = command.name().getSet();
        if (nameSet == null) {
            return this.mapper.toView(category);
        }

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = category.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = UpdateCategoryInfoService.applyChanges(category, nameSet);
        if (next == null) {
            return this.mapper.toView(category);
        }

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new CategoryUpdated(saved.getId()));

        return this.mapper.toView(saved);
    }

    private static @Nullable Category applyChanges(
            final Category current,
            final Change.Set<CategoryName> nameSet) {
        if (nameSet.value().equals(current.getName())) {
            return null;
        }

        return new Category(
                current.getId(),
                nameSet.value(),
                current.getImageKey(),
                current.getVersion(),
                current.getDeletionTime());
    }
}
