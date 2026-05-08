package vn.uit.edu.msshop.notification.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentLinkCreated {
    private UUID eventId;
    private String paymentLink;
    private UUID orderId;
    private String userEmail;
    private UUID userId;
}
