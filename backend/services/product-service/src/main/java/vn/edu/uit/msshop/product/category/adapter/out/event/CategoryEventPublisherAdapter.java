package vn.edu.uit.msshop.product.category.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.out.event.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreated;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdated;
import vn.edu.uit.msshop.product.category.domain.event.CategoryPurged;
import vn.edu.uit.msshop.product.category.domain.event.CategoryRestored;
import vn.edu.uit.msshop.product.category.domain.event.CategorySoftDeleted;
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

    @Override
    public void publish(
            final CategoryImageUpdated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final CategorySoftDeleted event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final CategoryRestored event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final CategoryPurged event) {
        this.publisher.publishEvent(event);
    }
}
