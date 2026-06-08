package vn.edu.uit.msshop.product.brand.application.port.out.event;

import vn.edu.uit.msshop.product.brand.domain.event.BrandEvent;

public interface BrandEventPublicationPort {
    void publishEvent(
            final BrandEvent event);
}
