package vn.uit.edu.msshop.order.application.port.in;

import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

public interface UpdateOrderUseCase {
    public void update(UpdateOrderCommand command);
    public void codOrderSuccess(OrderId orderId);
    public void codOrderCancelled(OrderId orderId);
}
