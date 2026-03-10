package vn.edu.uit.msshop.product.product.application.exception;

import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.shared.application.exception.NotFoundException;

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
