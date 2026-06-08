package vn.edu.uit.msshop.product.brand.domain.event;

import java.time.Instant;
import java.util.UUID;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public sealed interface BrandEvent
        permits
        BrandCreatedEvent,
        BrandInfoUpdatedEvent,
        BrandSoftDeletedEvent,
        BrandRestoredEvent,
        BrandHardDeletedEvent,
        BrandLogoUpdatedEvent {

    UUID getEventId();

    Instant getOccurrenceTime();

    BrandId getBrandId();

}
