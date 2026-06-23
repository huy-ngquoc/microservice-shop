package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@RequiredArgsConstructor
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantSoftDeletedEvent
        implements VariantEvent {

    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final VariantId variantId;

    private final VariantProductId productId;

    private final VariantSoldCountValue soldCountValue;

    private final VariantStockCountValue stockCountValue;

    public static VariantSoftDeletedEvent of(
            final Variant variant,
            final VariantSoldCount soldCount,
            final VariantStockCount stockCount) {
        return new VariantSoftDeletedEvent(
                variant.getId(),
                variant.getProductId(),
                soldCount.getValue(),
                stockCount.getValue());
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

    public VariantSoldCountValue getSoldCountValue() {
        return this.soldCountValue;
    }

    public VariantStockCountValue getStockCountValue() {
        return this.stockCountValue;
    }
}
