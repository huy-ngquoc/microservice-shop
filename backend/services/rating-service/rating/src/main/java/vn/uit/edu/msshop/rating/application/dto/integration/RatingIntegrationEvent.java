package vn.uit.edu.msshop.rating.application.dto.integration;

public sealed interface RatingIntegrationEvent
        permits
        RatingCreatedIntegrationEvent,
        RatingUpdatedIntegrationEvent,
        RatingDeletedIntegrationEvent {
    String getAggregateId();
}
