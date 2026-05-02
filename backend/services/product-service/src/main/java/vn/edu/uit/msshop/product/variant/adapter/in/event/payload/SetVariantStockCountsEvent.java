package vn.edu.uit.msshop.product.variant.adapter.in.event.payload;

import java.util.Collection;
import java.util.UUID;

public record SetVariantStockCountsEvent(
        UUID eventId,
        Collection<Detail> details) {
    public record Detail(
            UUID variantId,
            int value) {
    }
}
