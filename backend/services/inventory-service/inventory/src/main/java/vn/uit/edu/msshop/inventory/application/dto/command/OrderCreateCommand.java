package vn.uit.edu.msshop.inventory.application.dto.command;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateCommand {
    private List<OrderDetailCommand> detailCommands;
    private UUID orderId;
}
