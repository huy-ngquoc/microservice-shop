package vn.edu.uit.msshop.product.category.adapter.out.persistence.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.adapter.out.persistence.CategoryDocument;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.category.domain.model.CategoryVersion;
import vn.edu.uit.msshop.product.category.domain.model.NewCategory;

@Component
public class CategoryPersistenceMapper {
    public Category toDomain(
            final CategoryDocument entity) {
        final var id = new CategoryId(entity.getId());
        final var name = new CategoryName(entity.getName());
        final var imageKey = CategoryImageKey.ofNullable(entity.getImageKey());

        final var versionValue = Objects.requireNonNull(
                entity.getVersion(),
                "Persisted category must have a version");
        final var version = new CategoryVersion(versionValue);

        return new Category(
                id,
                name,
                imageKey,
                version);
    }

    public CategoryDocument toPersistence(
            final Category category) {
        return new CategoryDocument(
                category.getId().value(),
                category.getName().value(),
                CategoryImageKey.unwrap(category.getImageKey()),
                category.getVersion().value());
    }

    public CategoryDocument toPersistence(
            final NewCategory newCategory) {
        return new CategoryDocument(
                newCategory.getId().value(),
                newCategory.getName().value(),
                null,
                null);
    }
}
