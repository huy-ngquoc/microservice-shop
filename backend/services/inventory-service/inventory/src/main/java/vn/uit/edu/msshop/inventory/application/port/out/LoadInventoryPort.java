package vn.uit.edu.msshop.inventory.application.port.out;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;


public interface LoadInventoryPort {
    public Optional<Inventory> loadById(InventoryId id);
    public Optional<Inventory> loadByVariantId(VariantId id);
    public Page<Inventory> loadAll(int pageNumber, int pageSize);
    public List<Inventory> findByListVariantId(List<VariantId> listVariantIds);
}
