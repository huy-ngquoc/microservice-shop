package vn.uit.edu.payment.adapter.out.event.documents;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="outbox_events_online_payment_expired")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlinePaymentExpiredDocument {
    @Id
    private UUID eventId;
    private UUID orderId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
}
