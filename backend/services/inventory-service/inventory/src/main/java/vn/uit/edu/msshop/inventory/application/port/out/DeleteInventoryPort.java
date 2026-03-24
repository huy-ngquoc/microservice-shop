package vn.uit.edu.msshop.inventory.application.port.out;

import vn.uit.edu.msshop.inventory.domain.model.Inventory;

public interface DeleteInventoryPort {
    public void delete(Inventory inventory);
}
