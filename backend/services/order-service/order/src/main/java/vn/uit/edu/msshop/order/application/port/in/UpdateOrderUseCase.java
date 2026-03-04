package vn.uit.edu.msshop.order.application.port.in;

import vn.uit.edu.msshop.order.application.dto.command.UpdateOrderCommand;

public interface UpdateOrderUseCase {
    public void update(UpdateOrderCommand command);
}
