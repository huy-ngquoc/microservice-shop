package vn.uit.edu.msshop.order.adapter.out.event.documents.inventory;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OutboxEvent;

@Document(collection="outbox_events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderCreatedInventoryDocument extends OutboxEvent {
    @Id
    private UUID eventId;
    private UUID orderId;
    private List<OrderDetailDocument> orderDetails;
}
