package vn.uit.edu.msshop.inventory.application.port.out;
import vn.uit.edu.msshop.inventory.domain.model.Inventory;


public interface SaveInventoryPort {
    public Inventory save(Inventory inventory);
}
