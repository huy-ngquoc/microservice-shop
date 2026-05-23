package vn.uit.edu.msshop.rating.application.dto.integration;

import java.time.Instant;
import java.util.UUID;

public record RatingCreatedIntegrationEvent(
        UUID eventId,
        UUID productId,
        int point,
        Instant occurrenceTime)
        implements
            RatingIntegrationEvent {

    @Override
    public String getAggregateId() {
        return this.eventId.toString();
    }
}
