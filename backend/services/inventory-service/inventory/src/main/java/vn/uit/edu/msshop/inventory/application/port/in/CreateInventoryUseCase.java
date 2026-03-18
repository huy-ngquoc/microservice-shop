package vn.uit.edu.msshop.inventory.application.port.in;

import vn.uit.edu.msshop.inventory.application.dto.command.CreateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public interface CreateInventoryUseCase {
    public InventoryView create(VariantId variantId);
    public InventoryView create(CreateInventoryCommand command);
}
