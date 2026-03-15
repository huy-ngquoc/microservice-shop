package vn.edu.uit.msshop.product.category.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.Category;

@Component
public class CategoryViewMapper {
    public CategoryView toView(
            final Category category) {
        final Long versionRawValue;
        if (category.getVersion() != null) {
            versionRawValue = category.getVersion().value();
        } else {
            versionRawValue = null;
        }

        return new CategoryView(
                category.getId().value(),
                category.getName().value(),
                category.getImageKey().value(),
                versionRawValue);
    }

    public CategoryImageView toImageView(
            final Category category) {
        final Long versionRawValue;
        if (category.getVersion() != null) {
            versionRawValue = category.getVersion().value();
        } else {
            versionRawValue = null;
        }

        return new CategoryImageView(
                category.getId().value(),
                category.getImageKey().value(),
                versionRawValue);
    }
}
