package vn.edu.uit.msshop.product.application.port.out;

import vn.edu.uit.msshop.product.domain.event.category.CategoryCreated;
import vn.edu.uit.msshop.product.domain.event.category.CategoryUpdated;

public interface PublishCategoryEventPort {
    void publish(
            final CategoryCreated event);

    void publish(
            final CategoryUpdated event);
}
