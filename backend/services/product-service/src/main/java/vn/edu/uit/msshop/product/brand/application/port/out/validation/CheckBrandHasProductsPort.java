package vn.edu.uit.msshop.product.brand.application.port.out.validation;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface CheckBrandHasProductsPort {
    boolean hasProducts(
            final BrandId id);
}
