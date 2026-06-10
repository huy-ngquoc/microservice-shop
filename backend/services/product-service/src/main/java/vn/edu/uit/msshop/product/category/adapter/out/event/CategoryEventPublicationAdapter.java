package vn.edu.uit.msshop.product.category.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryEvent;

@Component
@RequiredArgsConstructor
public class CategoryEventPublicationAdapter
        implements CategoryEventPublicationPort {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publishEvent(
            final CategoryEvent event) {
        this.publisher.publishEvent(event);
    }
}
