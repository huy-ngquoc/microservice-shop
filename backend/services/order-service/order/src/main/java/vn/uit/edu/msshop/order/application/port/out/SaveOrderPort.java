package vn.uit.edu.msshop.order.application.port.out;

import vn.uit.edu.msshop.order.domain.model.Order;

public interface SaveOrderPort {
    public Order save(Order order);
}
