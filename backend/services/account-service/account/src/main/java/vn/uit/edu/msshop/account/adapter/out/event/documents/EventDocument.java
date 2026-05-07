package vn.uit.edu.msshop.account.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name="event_document")
public class EventDocument {
    @Id
    private UUID eventId;
    private Instant receiveAt;
}