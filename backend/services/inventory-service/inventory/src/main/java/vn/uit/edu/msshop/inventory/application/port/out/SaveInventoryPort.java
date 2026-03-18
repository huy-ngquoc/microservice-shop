package vn.uit.edu.msshop.inventory.application.port.out;
import java.util.List;

import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.Quantity;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;


public interface SaveInventoryPort {
    public Inventory save(Inventory inventory);
    public Inventory createNew(VariantId variantId);
    public List<Inventory> saveAll(List<Inventory> inventory);
    public Inventory createFromCommand(VariantId variantId, Quantity quantity);
}
