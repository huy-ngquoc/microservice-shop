package vn.edu.uit.msshop.product.category.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.adapter.out.persistence.CategoryDocument;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.category.domain.model.CategoryVersion;

@Component
public class CategoryPersistenceMapper {
    public Category toDomain(
            final CategoryDocument entity) {
        final var id = new CategoryId(entity.getId());
        final var name = new CategoryName(entity.getName());
        final var imageKey = new CategoryImageKey(entity.getImageKey());

        final CategoryVersion version;
        if (entity.getVersion() != null) {
            version = new CategoryVersion(entity.getVersion());
        } else {
            version = null;
        }

        return new Category(
                id,
                name,
                imageKey,
                version);
    }

    public CategoryDocument toPersistence(
            final Category category) {
        final Long versionRawValue;
        if (category.getVersion() != null) {
            versionRawValue = category.getVersion().value();
        } else {
            versionRawValue = null;
        }

        return new CategoryDocument(
                category.getId().value(),
                category.getName().value(),
                category.getImageKey().value(),
                versionRawValue);
    }
}
