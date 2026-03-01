package vn.edu.uit.msshop.product.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.domain.event.brand.BrandCreated;
import vn.edu.uit.msshop.product.domain.event.brand.BrandUpdated;

@Component
@RequiredArgsConstructor
public class BrandEventPublisherAdapter
        implements PublishBrandEventPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(
            final BrandCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final BrandUpdated event) {
        this.publisher.publishEvent(event);
    }
}
