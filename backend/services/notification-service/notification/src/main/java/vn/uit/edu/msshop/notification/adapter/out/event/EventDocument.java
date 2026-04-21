package vn.uit.edu.msshop.notification.adapter.out.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="event_document")
public class EventDocument {
    @Id
    private UUID eventId;
    @Indexed(expireAfter="86400000")
    private Instant receiveAt;
}
