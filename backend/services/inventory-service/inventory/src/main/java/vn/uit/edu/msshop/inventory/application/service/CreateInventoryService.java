package vn.uit.edu.msshop.inventory.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.dto.command.CreateInventoryCommand;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.mapper.InventoryViewMapper;
import vn.uit.edu.msshop.inventory.application.port.in.CreateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.PublishInventoryEventPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.domain.event.InventoryUpdated;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class CreateInventoryService implements CreateInventoryUseCase {
    private final InventoryViewMapper mapper;
    private final LoadInventoryPort loadPort;
    private final SaveInventoryPort savePort;
    private final PublishInventoryEventPort publishPort;

    @Override
    public InventoryView create(VariantId variantId) {
        Optional<Inventory> inventoryExist = loadPort.loadByVariantId(variantId);
        if(inventoryExist.isPresent()) return null;
        final var result = savePort.createNew(variantId);
        return mapper.toView(result);
        
    }

    @Override
    public InventoryView create(CreateInventoryCommand command) {
        Optional<Inventory> inventoryExist = loadPort.loadByVariantId(command.variantId());
        if(inventoryExist.isPresent()) return null;
        final var result = savePort.createFromCommand(command.variantId(), command.quantity());
        publishPort.publishInventoryUpdateEvent(new InventoryUpdated(command.variantId().value(),command.quantity().value(),0));
        return mapper.toView(result);

    }

}
