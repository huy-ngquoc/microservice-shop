package vn.uit.edu.msshop.inventory.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.application.mapper.InventoryViewMapper;
import vn.uit.edu.msshop.inventory.application.port.in.CreateInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.SaveInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class CreateInventoryService implements CreateInventoryUseCase {
    private final InventoryViewMapper mapper;
    private final LoadInventoryPort loadPort;
    private final SaveInventoryPort savePort;

    @Override
    public InventoryView create(VariantId variantId) {
        Optional<Inventory> inventoryExist = loadPort.loadByVariantId(variantId);
        if(inventoryExist.isPresent()) return null;
        final var result = savePort.createNew(variantId);
        return mapper.toView(result);
        
    }

}
