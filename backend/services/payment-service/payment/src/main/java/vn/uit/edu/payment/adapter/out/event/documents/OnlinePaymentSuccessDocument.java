package vn.uit.edu.payment.adapter.out.event.documents;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="online_payment_success")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlinePaymentSuccessDocument {
    @Id
    private UUID eventId;
    private UUID orderId;
    private UUID userId;
    private String userEmail;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
    
}
