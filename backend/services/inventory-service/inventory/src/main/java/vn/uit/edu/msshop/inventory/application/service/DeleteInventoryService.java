package vn.uit.edu.msshop.inventory.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.exception.InventoryNotFoundException;
import vn.uit.edu.msshop.inventory.application.port.in.DeleteInventoryUseCase;
import vn.uit.edu.msshop.inventory.application.port.out.DeleteInventoryPort;
import vn.uit.edu.msshop.inventory.application.port.out.LoadInventoryPort;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class DeleteInventoryService implements DeleteInventoryUseCase {
    private final LoadInventoryPort loadPort;
    private final DeleteInventoryPort deletePort;

    @Override
    @Transactional
    public void deleteByVariantId(
            VariantId id) {
        Inventory inventory = loadPort.loadByVariantId(id).orElseThrow(() -> new InventoryNotFoundException(id));
        deletePort.delete(inventory);
    }

}
