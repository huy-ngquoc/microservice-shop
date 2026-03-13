package vn.edu.uit.msshop.product.category.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.Category;

@Component
public class CategoryViewMapper {
    public CategoryView toView(
            final Category category) {
        return new CategoryView(
                category.getId().value(),
                category.getName().value(),
                category.getImageKey().value());
    }
}
