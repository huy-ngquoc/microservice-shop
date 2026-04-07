package vn.uit.edu.msshop.inventory.application.port.out;

import java.util.UUID;

import vn.uit.edu.msshop.inventory.domain.model.Inventory;

public interface SyncInventoryPort {
    public Inventory loadFromMainDatabase(UUID variantId);
}
