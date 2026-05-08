package vn.uit.edu.msshop.order.adapter.out.event.documents;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.uit.edu.msshop.order.domain.event.OrderDetailEvent;

@Document(collection="order_updated_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderUpdatedEventDocument extends OutboxEvent {
    @Id
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
