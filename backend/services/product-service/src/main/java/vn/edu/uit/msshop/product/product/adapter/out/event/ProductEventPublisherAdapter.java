package vn.edu.uit.msshop.product.product.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.event.ProductRestored;
import vn.edu.uit.msshop.product.product.domain.event.ProductSoftDeleted;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;

@Component
@RequiredArgsConstructor
public class ProductEventPublisherAdapter
        implements PublishProductEventPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(
            final ProductCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final ProductUpdated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final ProductSoftDeleted event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final ProductRestored event) {
        this.publisher.publishEvent(event);
    }
}
