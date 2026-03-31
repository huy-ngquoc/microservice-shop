package vn.uit.edu.msshop.account.adapter.out.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection="outbox_events")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventDocument {
    @Id
    private UUID eventId;
    private Instant receiveAt;
}