package vn.uit.edu.msshop.inventory.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.inventory.domain.model.OrderDetail;



public interface ProcessOrderUseCase {
    public void processOrder(List<OrderDetail> orderDetail);
}
