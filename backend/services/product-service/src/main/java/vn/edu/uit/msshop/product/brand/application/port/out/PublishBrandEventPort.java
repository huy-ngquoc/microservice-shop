package vn.edu.uit.msshop.product.brand.application.port.out;

import vn.edu.uit.msshop.product.brand.domain.event.BrandCreated;
import vn.edu.uit.msshop.product.brand.domain.event.BrandUpdated;

public interface PublishBrandEventPort {
    void publish(
            final BrandCreated event);

    void publish(
            final BrandUpdated event);
}
