package vn.uit.edu.msshop.inventory.application.port.in;

import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;

public interface UpdateInventoryUseCase {
    public InventoryView update(UpdateInventoryCommand command);
}
