package vn.uit.edu.msshop.rating.application.dto.integration;

import java.time.Instant;
import java.util.UUID;

public record RatingUpdatedIntegrationEvent(
        UUID eventId,
        UUID productId,
        int oldPoint,
        int newPoint,
        Instant occurrenceTime)
        implements
            RatingIntegrationEvent {

    @Override
    public String getAggregateId() {
        return this.eventId.toString();
    }
}
