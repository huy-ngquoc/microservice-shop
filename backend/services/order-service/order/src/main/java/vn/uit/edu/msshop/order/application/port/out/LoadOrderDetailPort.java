package vn.uit.edu.msshop.order.application.port.out;

import java.util.List;
import java.util.UUID;

import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;

public interface LoadOrderDetailPort {
    public OrderDetail loadOrderDetail(UUID variantId, int quantity);
    public List<OrderDetail> loadByListDetail(List<OrderDetailRequest> requests);
}
