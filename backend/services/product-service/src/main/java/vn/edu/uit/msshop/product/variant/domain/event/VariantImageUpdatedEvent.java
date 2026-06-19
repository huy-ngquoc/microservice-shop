package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@RequiredArgsConstructor
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantImageUpdatedEvent
        implements VariantEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final VariantId variantId;

    private final VariantProductId productId;

    @Nullable
    private final VariantImageKey newImageKey;

    @Nullable
    private final VariantImageKey oldImageKey;

    public static VariantImageUpdatedEvent of(
            final Variant variant,
            @Nullable
            final VariantImageKey oldImageKey) {
        return new VariantImageUpdatedEvent(
                variant.getId(),
                variant.getProductId(),
                variant.getImageKey(),
                oldImageKey);
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

    public @Nullable VariantImageKey getNewImageKey() {
        return this.newImageKey;
    }

    public @Nullable VariantImageKey getOldImageKey() {
        return this.oldImageKey;
    }

}
