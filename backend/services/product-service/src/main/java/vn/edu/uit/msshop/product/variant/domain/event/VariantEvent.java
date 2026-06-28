package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public sealed interface VariantEvent
        permits
        VariantCreatedEvent,
        VariantInfoUpdatedEvent,
        VariantSoftDeletedEvent,
        VariantRestoredEvent,
        VariantHardDeletedEvent,
        VariantImageUpdatedEvent,
        VariantInfoUpdatedForProductEvent,
        VariantSoftDeletedForProductEvent,
        VariantRestoredForProductEvent {

    UUID getEventId();

    Instant getOccurrenceTime();

    VariantId getVariantId();

    VariantProductId getProductId();

}
