package vn.uit.edu.msshop.notification.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnlinePaymentSuccess {
    private UUID eventId;
    private UUID orderId;
    private String userEmail;
    private UUID userId;
}
