package vn.uit.edu.msshop.order.adapter.out.event;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreatedDocument {
    @Id
    private UUID eventId;
    private String currency;
    private UUID orderId;
    private String paymentMethod;
    private long paymentValue;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;

}
