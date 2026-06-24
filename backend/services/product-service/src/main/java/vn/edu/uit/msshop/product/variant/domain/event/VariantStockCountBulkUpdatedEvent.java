package vn.edu.uit.msshop.product.variant.domain.event;

import java.util.List;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@Getter
public final class VariantStockCountBulkUpdatedEvent
        implements VariantCountBulkUpdatedEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final List<VariantStockCountDelta> deltaList;

    public VariantStockCountBulkUpdatedEvent(
            final List<VariantStockCountDelta> deltaList) {
        this.deltaList = List.copyOf(deltaList);
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    public List<VariantStockCountDelta> getDeltaList() {
        return this.deltaList;
    }

    public record VariantStockCountDelta(
            UUID variantId,
            UUID productId,
            int delta) {
    }

}
