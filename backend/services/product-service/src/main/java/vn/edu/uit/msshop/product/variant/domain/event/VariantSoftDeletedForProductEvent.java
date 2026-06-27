package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@RequiredArgsConstructor
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantSoftDeletedForProductEvent
        implements VariantEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final VariantId variantId;

    private final VariantProductId productId;

    public static VariantSoftDeletedForProductEvent of(
            final Variant variant) {
        return new VariantSoftDeletedForProductEvent(
                variant.getId(),
                variant.getProductId());
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
}
