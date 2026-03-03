package vn.edu.uit.msshop.product.category.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreated;
import vn.edu.uit.msshop.product.category.domain.event.CategoryUpdated;

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
