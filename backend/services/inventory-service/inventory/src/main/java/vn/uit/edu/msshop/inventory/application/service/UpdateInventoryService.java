package vn.uit.edu.msshop.inventory.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.dto.command.UpdateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.mapper.InventoryViewMapper;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;

@Service
@RequiredArgsConstructor
public class UpdateInventoryService implements UpdateInventoryUseCase {
    private final SaveInventoryPort savePort;
    private final LoadInventoryPort loadPort;
    private final InventoryViewMapper mapper; 

    @Override
    public InventoryView update(UpdateInventoryCommand command) {
        Inventory inventory = loadPort.loadByVariantId(command.variantId()).orElseThrow(()->new InventoryNotFoundException(command.variantId()));
        final var u = Inventory.UpdateInfo.builder().inventoryId(inventory.getId()).quantity(command.quantity().apply(inventory.getQuantity())).reservedQuantity(command.reservedQuantity().apply(inventory.getReservedQuantity())).build();
        final var next = inventory.applyUpdateInfo(u);
        final var saved = savePort.save(next);
        return mapper.toView(saved);
    }

}
