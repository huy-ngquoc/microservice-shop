package vn.edu.uit.msshop.product.product.application.port.out.event;

import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.event.ProductPurged;
import vn.edu.uit.msshop.product.product.domain.event.ProductRestored;
import vn.edu.uit.msshop.product.product.domain.event.ProductSoftDeleted;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;

public interface PublishProductEventPort {
    void publish(
            final ProductCreated event);

    void publish(
            final ProductUpdated event);

    void publish(
            final ProductSoftDeleted event);

    void publish(
            final ProductRestored event);

    void publish(
            final ProductPurged event);
}
