package vn.edu.uit.msshop.product.category.application.mapper;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryVersion;

@Component
public class CategoryViewMapper {
    public CategoryView toView(
            final Category category) {
        return new CategoryView(
                category.getId().value(),
                category.getName().value(),
                CategoryImageKey.unwrap(category.getImageKey()),
                CategoryVersion.unwrap(category.getVersion()));
    }

    public CategoryImageView toImageView(
            final Category category) {
        return new CategoryImageView(
                category.getId().value(),
                CategoryImageKey.unwrap(category.getImageKey()),
                CategoryVersion.unwrap(category.getVersion()));
    }
}
