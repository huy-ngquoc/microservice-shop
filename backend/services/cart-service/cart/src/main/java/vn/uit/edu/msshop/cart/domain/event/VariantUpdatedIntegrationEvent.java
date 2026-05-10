package vn.uit.edu.msshop.cart.domain.event;

import java.util.List;
import java.util.UUID;

public record VariantUpdatedIntegrationEvent(
    UUID eventId,
        UUID variantId,
        List<String> traits,
        long unitPrice,
        String name,
        String imageKey
) {

}
