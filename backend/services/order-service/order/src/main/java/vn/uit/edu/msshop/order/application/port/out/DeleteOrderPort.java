package vn.uit.edu.msshop.order.application.port.out;

import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

public interface DeleteOrderPort {
    public void deleteById(OrderId orderId);
    public void deleteAll();
}
