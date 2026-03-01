package vn.edu.uit.msshop.product.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.domain.event.category.CategoryCreated;
import vn.edu.uit.msshop.product.domain.event.category.CategoryUpdated;

@Component
@RequiredArgsConstructor
public class CategoryEventPublisherAdapter
        implements PublishCategoryEventPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(
            final CategoryCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final CategoryUpdated event) {
        this.publisher.publishEvent(event);
    }
}
