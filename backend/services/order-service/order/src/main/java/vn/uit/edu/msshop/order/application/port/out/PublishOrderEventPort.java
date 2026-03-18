package vn.uit.edu.msshop.order.application.port.out;


import vn.uit.edu.msshop.order.domain.event.CodPaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderCreatedSuccess;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderCancelled;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderShipped;

public interface PublishOrderEventPort {
   
    public void publish(OrderUpdated orderUpdated);

    public void publishOrderCreatedEvent(OrderCreated event);

    public void publishCodPaymentCancelled(CodPaymentCancelled event);
    public void publishCodPaymentReceived(CodPaymentReceived event);
    public void publishClearCartEvent(OrderCreatedSuccess event);

    public void publishOrderCreated_InventoryEvent(vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated event);
    public void publishOrderCancelled_InventoryEvent(OrderCancelled event);
    public void publishOrderShipped_InventoryEvent(OrderShipped event);

    
}
