package vn.edu.uit.msshop.product.product.domain.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class ProductOptionAddedEvent
        implements ProductEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final ProductId productId;

    private final Map<ProductVariantId, ProductVariantTraits> traitsByVariantId;

    public ProductOptionAddedEvent(
            final ProductId productId,
            final Map<ProductVariantId, ProductVariantTraits> traitsByVariantId) {
        this.productId = productId;
        this.traitsByVariantId = Map.copyOf(traitsByVariantId);
    }

    public UUID getEventId() {
        return this.eventId;
    }

    public Instant getOccurrenceTime() {
        return this.occurrenceTime;
    }

    public ProductId getProductId() {
        return this.productId;
    }

    public Map<ProductVariantId, ProductVariantTraits> getTraitsByVariantId() {
        return this.traitsByVariantId;
    }

}
