package vn.uit.edu.msshop.order.application.exception;

import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(OrderId orderId) {
        super("Order not found with id "+orderId.value().toString());
    }
}
