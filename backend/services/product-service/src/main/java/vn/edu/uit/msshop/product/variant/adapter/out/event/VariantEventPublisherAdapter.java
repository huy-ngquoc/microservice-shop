package vn.edu.uit.msshop.product.variant.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.event.VariantImageUpdated;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestored;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;

@Component
@RequiredArgsConstructor
public class VariantEventPublisherAdapter
        implements PublishVariantEventPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(
            final VariantCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final VariantUpdated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final VariantImageUpdated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final VariantSoftDeleted event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final VariantRestored event) {
        this.publisher.publishEvent(event);
    }
}
