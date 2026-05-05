package vn.edu.uit.msshop.product.brand.application.exception;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.application.exception.NotFoundException;

public final class BrandLogoKeyNotFoundException extends NotFoundException {
    public BrandLogoKeyNotFoundException(
            final BrandLogoKey logoKey,
            final Throwable cause) {
        super("Brand logo key", logoKey.value(), cause);
    }

    public BrandLogoKeyNotFoundException(
            final BrandLogoKey logoKey) {
        super("Brand logo key", logoKey.value());
    }
}
