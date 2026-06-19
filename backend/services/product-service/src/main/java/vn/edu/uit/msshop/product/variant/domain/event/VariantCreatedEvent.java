package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@RequiredArgsConstructor
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantCreatedEvent
        implements VariantEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final VariantId variantId;

    private final VariantProductId productId;

    private final VariantPrice price;

    private final VariantTraits traits;

    public static VariantCreatedEvent of(
            final Variant variant) {
        return new VariantCreatedEvent(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getTraits());
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public Instant getOccurrenceTime() {
        return this.occurrenceTime;
    }

    @Override
    public VariantId getVariantId() {
        return this.variantId;
    }

    @Override
    public VariantProductId getProductId() {
        return this.productId;
    }

    public VariantPrice getPrice() {
        return this.price;
    }

    public VariantTraits getTraits() {
        return this.traits;
    }

}
