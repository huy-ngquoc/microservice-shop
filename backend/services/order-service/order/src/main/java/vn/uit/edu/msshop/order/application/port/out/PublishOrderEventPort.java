package vn.uit.edu.msshop.order.application.port.out;



import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentReceivedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.IncreaseSoldCountEventsDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderCreatedInventoryDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderShippedDocument;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;

public interface PublishOrderEventPort {
   
    public void publish(OrderUpdated orderUpdated);

    public void publishOrderCreatedEvent( OrderCreatedDocument outboxEvent);

    public void publishCodPaymentCancelled(CodPaymentCancelledDocument outboxEvent);
    public void publishCodPaymentReceived(CodPaymentReceivedDocument outboxEvent);
    public void publishClearCartEvent(OrderCreatedSuccessDocument outboxEvent);

    public void publishOrderCreated_InventoryEvent(OrderCreatedInventoryDocument outboxEvent);
    public void publishOrderCancelled_InventoryEvent(OrderCancelledDocument outboxEvent);
    public void publishOrderShipped_InventoryEvent(OrderShippedDocument outboxEvent);
    public void publishIncreaseSoldCountEvent(IncreaseSoldCountEventsDocument outboxEvent);

    
}
