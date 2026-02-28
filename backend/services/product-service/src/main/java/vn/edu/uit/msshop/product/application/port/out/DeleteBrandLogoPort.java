package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogoKey;

public interface DeleteBrandLogoPort {
    void deleteByKey(
            final BrandLogoKey key);
}
