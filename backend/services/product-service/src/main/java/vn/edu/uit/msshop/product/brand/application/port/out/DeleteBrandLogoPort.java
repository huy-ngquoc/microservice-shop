package vn.edu.uit.msshop.product.brand.application.port.out;

import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;

public interface DeleteBrandLogoPort {
    void deleteByKey(
            final BrandLogoKey key);
}
