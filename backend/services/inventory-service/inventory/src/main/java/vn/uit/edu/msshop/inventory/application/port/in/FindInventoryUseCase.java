package vn.uit.edu.msshop.inventory.application.port.in;

import org.springframework.data.domain.Page;

import vn.uit.edu.msshop.inventory.application.dto.query.InventoryView;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public interface FindInventoryUseCase {
    public InventoryView findById(InventoryId id);
    public InventoryView findByVariantId(VariantId id);
    public Page<InventoryView> findAll(int pageNumber, int pageSize);
}
