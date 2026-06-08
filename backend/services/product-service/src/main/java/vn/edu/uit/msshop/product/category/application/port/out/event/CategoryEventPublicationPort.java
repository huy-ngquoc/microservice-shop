package vn.edu.uit.msshop.product.category.application.port.out.event;

import vn.edu.uit.msshop.product.category.domain.event.CategoryEvent;

public interface CategoryEventPublicationPort {
    void publishEvent(
            final CategoryEvent event);
}
