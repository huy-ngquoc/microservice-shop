package vn.edu.uit.msshop.product.variant.application.port.out.event;

import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.event.VariantImageUpdated;
import vn.edu.uit.msshop.product.variant.domain.event.VariantPurged;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestored;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;

public interface PublishVariantEventPort {
    void publish(
            final VariantCreated event);

    void publish(
            final VariantUpdated event);

    void publish(
            final VariantImageUpdated event);

    void publish(
            final VariantSoftDeleted event);

    void publish(
            final VariantRestored event);

    void publish(
            final VariantPurged event);
}
