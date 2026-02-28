package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.event.brand.BrandCreated;
import vn.edu.uit.msshop.product.domain.event.brand.BrandUpdated;

public interface PublishBrandEventPort {
    void publish(
            final BrandCreated event);

    void publish(
            final BrandUpdated event);
}
