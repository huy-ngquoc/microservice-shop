package vn.uit.edu.msshop.inventory.application.dto.command;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.OrderStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelledCommand {
    private List<OrderDetailCommand> detailCommands;
    private UUID orderId;
    private OrderStatus orderStatus;
}
