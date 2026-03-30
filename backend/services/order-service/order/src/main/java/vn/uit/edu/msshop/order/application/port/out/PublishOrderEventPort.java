package vn.uit.edu.msshop.order.application.port.out;


import vn.uit.edu.msshop.order.adapter.out.event.CodPaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.CodPaymentReceivedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.inventory.OrderCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.inventory.OrderCreatedInventoryDocument;
import vn.uit.edu.msshop.order.adapter.out.event.inventory.OrderShippedDocument;
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

    
}
