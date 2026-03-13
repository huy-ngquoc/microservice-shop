package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.event.CategoryCreated;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdated;
import vn.edu.uit.msshop.product.category.domain.event.CategoryUpdated;

public interface PublishCategoryEventPort {
    void publish(
            final CategoryCreated event);

    void publish(
            final CategoryUpdated event);

    void publish(
            final CategoryImageUpdated event);
}
