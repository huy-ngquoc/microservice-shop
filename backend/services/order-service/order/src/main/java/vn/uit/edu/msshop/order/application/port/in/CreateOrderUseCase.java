package vn.uit.edu.msshop.order.application.port.in;

import vn.uit.edu.msshop.order.application.dto.command.CreateOrderCommand;

public interface CreateOrderUseCase {
    public void create(CreateOrderCommand command);
}
