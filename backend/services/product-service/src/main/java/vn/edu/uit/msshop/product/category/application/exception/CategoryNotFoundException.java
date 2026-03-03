package vn.edu.uit.msshop.product.category.application.exception;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.shared.application.exception.NotFoundException;

public final class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(
            final CategoryId id,
            final Throwable cause) {
        super(Category.class.getSimpleName(), id.value().toString(), cause);
    }

    public CategoryNotFoundException(
            final CategoryId id) {
        super(Category.class.getSimpleName(), id.value().toString());
    }
}
