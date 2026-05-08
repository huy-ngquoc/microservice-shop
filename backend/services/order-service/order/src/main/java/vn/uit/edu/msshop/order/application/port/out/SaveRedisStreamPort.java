package vn.uit.edu.msshop.order.application.port.out;

import vn.uit.edu.msshop.order.adapter.out.persistence.OrderDocument;
import vn.uit.edu.msshop.order.domain.model.Order;

public interface SaveRedisStreamPort {
    public void saveToStream(Order order);
}
