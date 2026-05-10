package vn.uit.edu.msshop.inventory.application.dto.command;

import vn.uit.edu.msshop.inventory.application.common.Change;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryStatus;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public record UpdateInventoryCommand(
        VariantId variantId,
        Change<Quantity> quantity,
        Change<ReservedQuantity> reservedQuantity,
        Change<InventoryStatus> newStatus) {
}
