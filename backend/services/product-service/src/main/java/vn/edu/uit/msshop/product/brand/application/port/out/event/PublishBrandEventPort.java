package vn.edu.uit.msshop.product.brand.application.port.out.event;

import vn.edu.uit.msshop.product.brand.domain.event.BrandCreated;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdated;
import vn.edu.uit.msshop.product.brand.domain.event.BrandSoftDeleted;
import vn.edu.uit.msshop.product.brand.domain.event.BrandUpdated;

public interface PublishBrandEventPort {
    void publish(
            final BrandCreated event);

    void publish(
            final BrandUpdated event);

    void publish(
            final BrandLogoUpdated event);

    void publish(
            final BrandSoftDeleted event);
}
