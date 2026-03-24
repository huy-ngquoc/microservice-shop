package vn.uit.edu.msshop.inventory.application.port.in;

import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public interface DeleteInventoryUseCase {
    public void deleteByVariantId(VariantId id);
}
