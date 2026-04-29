package vn.uit.edu.msshop.inventory.application.port.out;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryStatus;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;


public interface LoadInventoryPort {
    public Optional<Inventory> loadById(InventoryId id);
    public Optional<Inventory> loadByVariantId(VariantId id);
    public Page<Inventory> loadAll(int pageNumber, int pageSize);
    public List<Inventory> findByListVariantId(List<VariantId> listVariantIds);
    public Optional<Inventory> loadByVariantIdAndStatus(VariantId id, InventoryStatus status);
    public List<Inventory> findAllByListVariantId(List<VariantId> listVariantIds);
    public Page<Inventory> findAllUpdatedInventory(Instant startFirst, Instant endFirst, Instant startSecond, Instant endSecond, Pageable pageable);
}
