package vn.uit.edu.msshop.inventory.application.dto.command;

import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public record CreateInventoryCommand(VariantId variantId, Quantity quantity) {

}
