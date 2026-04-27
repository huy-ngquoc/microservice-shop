package vn.uit.edu.msshop.order.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreatedFail {
    private UUID eventId;
    private UUID orderId;
    private UUID userId;
    private String userEmail;

}
