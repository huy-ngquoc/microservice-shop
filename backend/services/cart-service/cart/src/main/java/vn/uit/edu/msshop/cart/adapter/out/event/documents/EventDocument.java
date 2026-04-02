package vn.uit.edu.msshop.cart.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection="outbox_collections")
@Getter
@Setter
@Builder
public class EventDocument {
    @Id
    private UUID eventId;
    private Instant receiveAt;
}
