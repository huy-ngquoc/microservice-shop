package vn.edu.uit.msshop.product.product.application.exception;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.shared.application.exception.NotFoundException;

public final class ProductCategoryNotFoundException extends NotFoundException {
    public ProductCategoryNotFoundException(
            final ProductCategoryId categoryId,
            final Throwable cause) {
        super("Product category", categoryId.value().toString(), cause);
    }

    public ProductCategoryNotFoundException(
            final ProductCategoryId categoryId) {
        super("Product category", categoryId.value().toString());
    }
}
