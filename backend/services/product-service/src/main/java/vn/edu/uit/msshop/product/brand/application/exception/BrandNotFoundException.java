package vn.edu.uit.msshop.product.brand.application.exception;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.shared.application.exception.NotFoundException;

public final class BrandNotFoundException extends NotFoundException {
    public BrandNotFoundException(
            final BrandId id,
            final Throwable cause) {
        super(Brand.class.getSimpleName(), id.value().toString(), cause);
    }

    public BrandNotFoundException(
            final BrandId id) {
        super(Brand.class.getSimpleName(), id.value().toString());
    }
}
