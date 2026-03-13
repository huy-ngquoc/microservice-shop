package vn.edu.uit.msshop.product.category.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.adapter.out.persistence.CategoryDocument;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;

@Component
public class CategoryPersistenceMapper {
    public Category toDomain(
            final CategoryDocument entity) {
        final var id = new CategoryId(entity.getId());
        final var name = new CategoryName(entity.getName());
        final var imageKey = new CategoryImageKey(entity.getImageKey());

        return new Category(
                id,
                name,
                imageKey);
    }

    public CategoryDocument toPersistence(
            final Category category) {
        return new CategoryDocument(
                category.getId().value(),
                category.getName().value(),
                category.getImageKey().value());
    }
}
