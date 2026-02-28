package vn.edu.uit.msshop.product.application.exception;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

public final class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(
            final BrandId id) {
        super("Brand not found with ID: " + id.value());
    }
}
