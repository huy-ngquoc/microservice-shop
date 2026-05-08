package vn.uit.edu.payment.adapter.out.event.documents;

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
@Table(
        name = "payment_success")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSuccessDocument {
    @Id
    private UUID eventId;
    private UUID orderId;
    private String eventStatus;
    private Integer retryCount;
    private Instant createdAt;
    private Instant updatedAt;
    private String lastError;
}
