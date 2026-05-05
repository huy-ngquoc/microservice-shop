package vn.edu.uit.msshop.product.shared.event.document;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "event_document")
public class EventDocument {
  @Id
  private UUID eventId;
  private Instant receiveAt;
}
