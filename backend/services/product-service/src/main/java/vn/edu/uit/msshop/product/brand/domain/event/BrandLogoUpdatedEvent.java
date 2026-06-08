package vn.edu.uit.msshop.product.brand.domain.event;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@RequiredArgsConstructor
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class BrandLogoUpdatedEvent
        implements BrandEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final BrandId brandId;

    @Nullable
    private final BrandLogoKey newLogoKey;

    @Nullable
    private final BrandLogoKey oldLogoKey;

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public Instant getOccurrenceTime() {
        return this.occurrenceTime;
    }

    @Override
    public BrandId getBrandId() {
        return this.brandId;
    }

    public @Nullable BrandLogoKey getNewLogoKey() {
        return this.newLogoKey;
    }

    public @Nullable BrandLogoKey getOldLogoKey() {
        return this.oldLogoKey;
    }

}
