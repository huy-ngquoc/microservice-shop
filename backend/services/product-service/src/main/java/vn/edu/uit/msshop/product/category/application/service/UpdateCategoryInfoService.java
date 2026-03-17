package vn.edu.uit.msshop.product.category.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryInfoUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryUpdated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.category.domain.model.CategoryVersion;
import vn.edu.uit.msshop.product.shared.application.dto.Change;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class UpdateCategoryInfoService implements UpdateCategoryInfoUseCase {
    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;
    private final CategoryViewMapper mapper;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
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

        final var next = this.applyChanges(category, nameSet, expectedVersion);
        if (next == null) {
            return this.mapper.toView(category);
        }

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new CategoryUpdated(saved.getId()));

        return this.mapper.toView(saved);
    }

    private @Nullable Category applyChanges(
            final Category current,
            final Change.Set<CategoryName> nameSet,
            final CategoryVersion expectedVersion) {
        if (nameSet.value().equals(current.getName())) {
            return null;
        }

        return new Category(
                current.getId(),
                nameSet.value(),
                current.getImageKey(),
                expectedVersion);
    }
}
