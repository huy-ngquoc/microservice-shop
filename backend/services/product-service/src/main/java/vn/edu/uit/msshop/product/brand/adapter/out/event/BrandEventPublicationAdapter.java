package vn.edu.uit.msshop.product.brand.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.out.event.BrandEventPublicationPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandEvent;

@Component
@RequiredArgsConstructor
public class BrandEventPublicationAdapter
        implements BrandEventPublicationPort {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publishEvent(
            final BrandEvent event) {
        this.publisher.publishEvent(event);
    }
}
