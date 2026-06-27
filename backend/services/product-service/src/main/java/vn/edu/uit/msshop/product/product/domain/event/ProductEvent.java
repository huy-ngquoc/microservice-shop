package vn.edu.uit.msshop.product.product.domain.event;

import java.time.Instant;
import java.util.UUID;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public sealed interface ProductEvent
        permits
        ProductCreatedEvent,
        ProductInfoUpdatedEvent,
        ProductSoftDeletedEvent,
        ProductRestoredEvent,
        ProductHardDeletedEvent,
        ProductNameChangedEvent,
        ProductOptionAddedEvent,
        ProductOptionRemovedEvent,
        ProductVariantBulkRemovedEvent {

    UUID getEventId();

    Instant getOccurrenceTime();

    ProductId getProductId();

}
