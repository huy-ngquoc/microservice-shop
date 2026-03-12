package vn.uit.edu.msshop.order.application.port.in;

import java.util.UUID;

import vn.uit.edu.msshop.order.application.dto.command.CreateOrderCommand;

public interface CreateOrderUseCase {
    public UUID create(CreateOrderCommand command);
}
