package vn.uit.edu.msshop.order.application.port.out;

import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;

public interface PublishOrderEventPort {
    public void publish(OrderCreated orderCreated);
    public void publish(OrderUpdated orderUpdated);
}
