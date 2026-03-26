package vn.edu.uit.msshop.product.product.application.exception;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.shared.application.exception.NotFoundException;

public final class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(
            final ProductId id,
            final Throwable cause) {
        super(Product.class.getSimpleName(), id.value().toString(), cause);
    }

    public ProductNotFoundException(
            final ProductId id) {
        super(Product.class.getSimpleName(), id.value().toString());
    }
}
