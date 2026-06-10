package vn.edu.uit.msshop.product.variant.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreatedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantImageUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantHardDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestoredEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantInfoUpdatedEvent;

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
