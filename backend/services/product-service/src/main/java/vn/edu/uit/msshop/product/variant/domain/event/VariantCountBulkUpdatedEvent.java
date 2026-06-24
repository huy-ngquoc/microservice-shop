package vn.edu.uit.msshop.product.variant.domain.event;

import java.util.UUID;

public sealed interface VariantCountBulkUpdatedEvent
        permits
        VariantSoldCountBulkUpdatedEvent,
        VariantStockCountBulkUpdatedEvent {
    UUID getEventId();
}
