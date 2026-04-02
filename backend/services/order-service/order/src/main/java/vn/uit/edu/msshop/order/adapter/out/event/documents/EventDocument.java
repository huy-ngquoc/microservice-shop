package vn.uit.edu.msshop.order.adapter.out.event.documents;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="outbox_events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventDocument {
    @Id
    private UUID eventId;
    private Instant receiveAt;
}
