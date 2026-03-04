package vn.uit.edu.msshop.order.application.port.out;

import java.util.UUID;

import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;

public interface LoadOrderDetailPort {
    public OrderDetail loadOrderDetail(UUID variantId, int quantity);
}
