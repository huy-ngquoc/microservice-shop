package vn.uit.edu.msshop.inventory.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.inventory.domain.model.OrderDetail;

public interface RollbackInventoryUseCase {
    public void orderCreateFail(List<OrderDetail> orderDetails);
    public void orderShippedFail(List<OrderDetail> orderDetails);
    public void orderCancelledFail(List<OrderDetail> orderDetails);
    public void orderCancelledWhenShippingFail(List<OrderDetail> orderDetails);
}
