package vn.edu.uit.msshop.product.variant.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantEvent;

@Component
@RequiredArgsConstructor
public class VariantEventPublisherAdapter
        implements VariantEventPublicationPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publishEvent(
            final VariantEvent event) {
        this.publisher.publishEvent(event);
    }

}
