package vn.edu.uit.msshop.product.product.adapter.in.event.payload;

public sealed interface RatingIntegrationEvent
        permits
        RatingCreatedIntegrationEvent,
        RatingUpdatedIntegrationEvent,
        RatingDeletedIntegrationEvent {
    String getAggregateId();
}
