package vn.edu.uit.msshop.product.product.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class ProductNameChangedEvent
        implements ProductEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final ProductId productId;

    private final ProductName newName;

    private ProductNameChangedEvent(
            final ProductId productId,
            final ProductName newName) {
        this.productId = productId;
        this.newName = newName;
    }

    public static ProductNameChangedEvent of(
            final Product product) {
        return new ProductNameChangedEvent(
                product.getId(),
                product.getName());
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

    public ProductName getNewName() {
        return this.newName;
    }

}
