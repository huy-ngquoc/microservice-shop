package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;

public interface PublishVariantEventPort {
    void publish(
            final VariantCreated event);

    void publish(
            final VariantUpdated event);
}
