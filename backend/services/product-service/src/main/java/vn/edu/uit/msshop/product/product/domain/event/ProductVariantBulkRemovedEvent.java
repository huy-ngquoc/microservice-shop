package vn.edu.uit.msshop.product.product.domain.event;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class ProductVariantBulkRemovedEvent
        implements ProductEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final ProductId productId;

    private final Set<ProductVariantId> removedVariantIdSet;

    public ProductVariantBulkRemovedEvent(
            final ProductId productId,
            final Set<ProductVariantId> removedVariantIdSet) {
        this.productId = productId;
        this.removedVariantIdSet = Set.copyOf(removedVariantIdSet);
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
    public ProductId getProductId() {
        return this.productId;
    }

    public Set<ProductVariantId> getRemovedVariantIdSet() {
        return this.removedVariantIdSet;
    }

}
