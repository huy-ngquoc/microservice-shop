package vn.edu.uit.msshop.product.variant.application.dto.integration;

import java.util.UUID;

public record VariantSoftDeletedIntegrationEvent(
        UUID eventId,
        UUID variantId) implements VariantIntegrationEvent {
    @Override
    public String getAggregateId() {
        return variantId.toString();
    }
}
