package vn.uit.edu.msshop.order.application.port.out;


import vn.uit.edu.msshop.order.domain.event.CodPaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;

public interface PublishOrderEventPort {
   
    public void publish(OrderUpdated orderUpdated);

    public void publishOrderCreatedEvent(OrderCreated event);

    public void publishCodPaymentCancelled(CodPaymentCancelled event);
    public void publishCodPaymentReceived(CodPaymentReceived event);

    
}
