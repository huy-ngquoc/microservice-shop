package vn.edu.uit.msshop.product.product.application.port.out.event;

import vn.edu.uit.msshop.product.product.domain.event.ProductEvent;

public interface ProductEventPublicationPort {

    void publishEvent(
            final ProductEvent event);

}
