package vn.edu.uit.msshop.product.application.exception;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

public final class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(
            final CategoryId id) {
        super("Category not found with ID: " + id.value());
    }
}
