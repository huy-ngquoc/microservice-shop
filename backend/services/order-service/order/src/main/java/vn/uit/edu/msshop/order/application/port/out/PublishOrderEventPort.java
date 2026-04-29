package vn.uit.edu.msshop.order.application.port.out;



import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;

public interface PublishOrderEventPort {
   
    public void publish(OrderUpdated orderUpdated);

    public void publishOrderCreatedEvent( OrderCreatedDocument outboxEvent);

    
    public void publishClearCartEvent(OrderCreatedSuccessDocument outboxEvent);

    

    
   

    public void publishOrderUpdatedEvent(OrderUpdatedEventDocument outboxEvent);

    
}
