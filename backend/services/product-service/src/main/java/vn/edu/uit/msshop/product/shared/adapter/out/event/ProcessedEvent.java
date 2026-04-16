package vn.edu.uit.msshop.product.shared.adapter.out.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ProcessedEvents")
public record ProcessedEvent(
        @Id
        UUID eventId,

        Instant processedAt) {
}
