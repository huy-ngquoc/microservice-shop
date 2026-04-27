package vn.uit.edu.msshop.inventory.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="event_document")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDocument {
    @Id
    private UUID eventId;
    private Instant receiveAt;
}
