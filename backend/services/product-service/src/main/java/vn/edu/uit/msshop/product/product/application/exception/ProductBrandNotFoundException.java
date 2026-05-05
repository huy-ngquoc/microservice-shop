package vn.edu.uit.msshop.product.product.application.exception;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.shared.application.exception.NotFoundException;

public final class ProductBrandNotFoundException extends NotFoundException {
    public ProductBrandNotFoundException(
            final ProductBrandId brandId,
            final Throwable cause) {
        super("Product brand", brandId.value().toString(), cause);
    }

    public ProductBrandNotFoundException(
            final ProductBrandId brandId) {
        super("Product brand", brandId.value().toString());
    }
}
