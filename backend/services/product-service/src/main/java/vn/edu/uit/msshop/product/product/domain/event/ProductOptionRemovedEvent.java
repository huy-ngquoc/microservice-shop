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
public final class ProductOptionRemovedEvent
        implements ProductEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final ProductId productId;

    private final Map<ProductVariantId, ProductVariantTraits> traitsByVariantId;

    public ProductOptionRemovedEvent(
            final ProductId productId,
            final Map<ProductVariantId, ProductVariantTraits> traitsByVariantId) {
        this.productId = productId;
        this.traitsByVariantId = Map.copyOf(traitsByVariantId);
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

    public Map<ProductVariantId, ProductVariantTraits> getTraitsByVariantId() {
        return this.traitsByVariantId;
    }
}
