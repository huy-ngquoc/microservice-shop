package vn.uit.edu.msshop.order.application.port.in;

import java.util.UUID;

import vn.uit.edu.msshop.order.application.dto.command.CreateOrderCommand;
import vn.uit.edu.msshop.order.domain.model.Order;

public interface CreateOrderUseCase {
    public UUID create(CreateOrderCommand command);
    public Order manualCreate(Order order);
}
