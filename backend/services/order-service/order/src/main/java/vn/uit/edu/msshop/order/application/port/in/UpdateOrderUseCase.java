package vn.uit.edu.msshop.order.application.port.in;

import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

public interface UpdateOrderUseCase {
    public void update(UpdateOrderCommand command, String userIdFromHeader, String role);
    public void codOrderSuccess(OrderId orderId, String userFromHeader);
    public void codOrderCancelled(OrderId orderId);
}
