package vn.uit.edu.msshop.inventory.adapter.out.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection="outbox_events")
@Getter
@Setter
@Builder
public class EventDocument {
    @Id
    private UUID eventId;
    private Instant receiveAt;
}
