package vn.edu.uit.msshop.product.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.domain.model.category.Category;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;

@Component
public class CategoryViewMapper {
    public CategoryView toView(
            final Category category) {
        final var image = category.getImage();

        String imageUrlValue = null;
        if (image != null) {
            imageUrlValue = image.url().value();
        }

        return new CategoryView(
                category.getId().value(),
                category.getName().value(),
                imageUrlValue);
    }

    public CategoryImageView toView(
            final CategoryImage image) {
        return new CategoryImageView(
                image.url().value(),
                image.size().width(),
                image.size().height());
    }
}
