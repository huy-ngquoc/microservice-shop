package vn.uit.edu.msshop.inventory.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.inventory.application.dto.command.OrderCancelledCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderCreateCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.OrderShippedCommand;
import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;

public interface UpdateInventoryUseCase {
    public InventoryView update(UpdateInventoryCommand command);
    public List<InventoryView> updateWhenOrderCreated(OrderCreateCommand command);
    public List<InventoryView> updateWhenOrderCancelled(OrderCancelledCommand command);
    public List<InventoryView> updateWhenOrderShipped(OrderShippedCommand command);
}
