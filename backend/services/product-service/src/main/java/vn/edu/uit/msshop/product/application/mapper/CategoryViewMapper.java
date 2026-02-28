package vn.edu.uit.msshop.product.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.domain.model.category.Category;

@Component
public class CategoryViewMapper {
    public CategoryView toView(
            final Category category) {
        return new CategoryView(
                category.getId().value(),
                category.getName().value(),
                category.getImage().url().value());
    }
}
