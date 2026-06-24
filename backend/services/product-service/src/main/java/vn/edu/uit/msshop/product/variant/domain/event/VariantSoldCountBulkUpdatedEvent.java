package vn.edu.uit.msshop.product.variant.domain.event;

import java.util.List;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantSoldCountBulkUpdatedEvent
        implements VariantCountBulkUpdatedEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final List<VariantSoldCountDelta> deltaList;

    public VariantSoldCountBulkUpdatedEvent(
            final List<VariantSoldCountDelta> deltaList) {
        this.deltaList = List.copyOf(deltaList);
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    public List<VariantSoldCountDelta> getDeltaList() {
        return this.deltaList;
    }

    public record VariantSoldCountDelta(
            UUID variantId,
            UUID productId,
            int delta) {
    }
}
