package vn.edu.uit.msshop.product.category.domain.event;

import java.time.Instant;
import java.util.UUID;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public sealed interface CategoryEvent
        permits
        CategoryCreatedEvent,
        CategoryInfoUpdatedEvent,
        CategorySoftDeletedEvent,
        CategoryRestoredEvent,
        CategoryHardDeletedEvent,
        CategoryImageUpdatedEvent {

    UUID getEventId();

    Instant getOccurrenceTime();

    CategoryId getCategoryId();

}
