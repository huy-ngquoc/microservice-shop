package vn.uit.edu.msshop.order.application.port.in;

import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

public interface DeleteOrderUseCase {
    public void deleteById(OrderId orderId);
    public void deleteAll();
}
