package vn.edu.uit.msshop.product.brand.application.port.out.logo;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;

public interface BrandLogoStoragePort {
    boolean existsAsTemp(
            final BrandLogoKey key);

    void publishLogo(
            final BrandLogoKey key);

    void unpublishLogo(
            final BrandLogoKey key);

    void deleteLogo(
            final BrandLogoKey key);
}
