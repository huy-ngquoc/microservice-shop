package vn.edu.uit.msshop.product.variant.adapter.in.event.payload;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record SetVariantSoldCountsEvent(
        UUID eventId,
        Collection<Detail> details) {
    public record Detail(
            UUID variantId,
            int newTotal) {
    }

    public SetVariantSoldCountsEvent {
        details = List.copyOf(details);
    }
}
