package vn.edu.uit.msshop.product.variant.application.port.out.event;

import vn.edu.uit.msshop.product.variant.domain.event.VariantEvent;

public interface VariantEventPublicationPort {

    void publishEvent(
            final VariantEvent event);

}
