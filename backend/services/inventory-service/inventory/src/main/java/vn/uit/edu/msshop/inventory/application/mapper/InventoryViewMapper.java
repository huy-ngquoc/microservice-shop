package vn.uit.edu.msshop.inventory.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;

@Component
public class InventoryViewMapper {
    public InventoryView toView(Inventory inventory) {
        return new InventoryView(inventory.getId().value(), inventory.getVariantId().value(), inventory.getQuantity().value(), inventory.getReservedQuantity().value(), inventory.getLastUpdate().value());
    }
}
