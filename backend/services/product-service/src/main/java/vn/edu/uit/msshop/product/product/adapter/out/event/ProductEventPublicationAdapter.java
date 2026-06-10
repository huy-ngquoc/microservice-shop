package vn.edu.uit.msshop.product.product.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductEvent;

@Component
@RequiredArgsConstructor
public class ProductEventPublicationAdapter
        implements ProductEventPublicationPort {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publishEvent(
            final ProductEvent event) {
        this.publisher.publishEvent(event);
    }

}
