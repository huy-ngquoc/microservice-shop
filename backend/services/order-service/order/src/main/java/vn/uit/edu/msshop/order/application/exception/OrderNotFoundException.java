package vn.uit.edu.msshop.order.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(OrderId orderId) {
        super("Order not found with id "+orderId.value().toString());
    }
}
