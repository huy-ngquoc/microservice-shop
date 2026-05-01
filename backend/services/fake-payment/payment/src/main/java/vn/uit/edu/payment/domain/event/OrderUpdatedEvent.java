package vn.uit.edu.payment.domain.event;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdatedEvent {
    private UUID eventId;
    private UUID orderId;

    private List<OrderDetailEvent> details;
    
    private String status;
    
    private UUID userId;

    private long originPrice;

    private long shippingFee;

    private long discount;

    private long totalPrice;

    

    

    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private String email;
    private String oldStatus;
}
