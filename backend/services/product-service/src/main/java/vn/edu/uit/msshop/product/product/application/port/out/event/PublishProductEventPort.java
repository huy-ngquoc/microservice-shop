package vn.edu.uit.msshop.product.product.application.port.out.event;

import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;

public interface PublishProductEventPort {
    void publish(
            final ProductCreated event);

    void publish(
            final ProductUpdated event);
}
