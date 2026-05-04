package vn.edu.uit.msshop.product.variant.application.dto.integration;

public sealed interface VariantIntegrationEvent
        permits
        VariantUpdatedIntegrationEvent,
        VariantSoftDeletedIntegrationEvent {
    String getAggregateId();
}
