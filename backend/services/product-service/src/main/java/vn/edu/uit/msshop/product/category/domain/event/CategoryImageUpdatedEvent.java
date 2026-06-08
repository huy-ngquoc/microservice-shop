package vn.edu.uit.msshop.product.category.domain.event;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@RequiredArgsConstructor
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class CategoryImageUpdatedEvent
        implements CategoryEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final CategoryId categoryId;

    @Nullable
    private final CategoryImageKey newImageKey;

    @Nullable
    private final CategoryImageKey oldImageKey;

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public Instant getOccurrenceTime() {
        return this.occurrenceTime;
    }

    @Override
    public CategoryId getCategoryId() {
        return this.categoryId;
    }

    public @Nullable CategoryImageKey getNewImageKey() {
        return this.newImageKey;
    }

    public @Nullable CategoryImageKey getOldImageKey() {
        return this.oldImageKey;
    }

}
