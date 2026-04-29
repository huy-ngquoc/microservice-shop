package vn.uit.edu.msshop.inventory.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.inventory.application.dto.command.CreateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.ReservedQuantity;

@Component
public class InventoryViewMapper {
    public InventoryView toView(Inventory inventory) {
        return new InventoryView(inventory.getId().value(), inventory.getVariantId().value(), inventory.getQuantity().value(), inventory.getReservedQuantity().value(), inventory.getLastUpdate().value(), inventory.getStatus().value(), inventory.getCreateAt().value());
    }
    public Inventory toDomain(CreateInventoryCommand command) {
        final var draft = Inventory.Draft.builder().id(new InventoryId(null)).variantId(command.variantId()).quantity(command.quantity()).reservedQuantity(new ReservedQuantity(0)).build();
        return Inventory.create(draft);
    }
}
