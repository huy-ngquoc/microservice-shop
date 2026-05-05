package vn.edu.uit.msshop.product.category.application.exception;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.shared.application.exception.NotFoundException;

public final class CategoryImageKeyNotFoundException extends NotFoundException {
    public CategoryImageKeyNotFoundException(
            final CategoryImageKey imageKey,
            final Throwable cause) {
        super("Category image key", imageKey.value(), cause);
    }

    public CategoryImageKeyNotFoundException(
            final CategoryImageKey imageKey) {
        super("Category image key", imageKey.value());
    }
}
