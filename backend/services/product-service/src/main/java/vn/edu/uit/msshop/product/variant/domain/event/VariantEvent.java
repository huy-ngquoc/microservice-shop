package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public sealed interface VariantEvent
        permits
        VariantCreatedEvent,
        VariantInfoUpdatedEvent,
        VariantSoftDeletedEvent,
        VariantRestoredEvent,
        VariantHardDeletedEvent,
        VariantImageUpdatedEvent {

    UUID getEventId();

    Instant getOccurrenceTime();

    VariantId getVariantId();

}
