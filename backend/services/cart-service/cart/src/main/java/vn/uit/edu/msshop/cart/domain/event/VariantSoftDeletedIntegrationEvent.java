package vn.uit.edu.msshop.cart.domain.event;

import java.util.UUID;

public record VariantSoftDeletedIntegrationEvent(
    UUID eventId,
        UUID variantId
) {

}
