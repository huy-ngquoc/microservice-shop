package vn.uit.edu.msshop.order.adapter.out.event.documents;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Document(collection="outbox_events_order_created_document")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderCreatedDocument extends OutboxEvent {
    @Id
    private UUID eventId;
    private String currency;
    private UUID orderId;
    private String paymentMethod;
    private long paymentValue;
    private UUID userId;

   
    

}
