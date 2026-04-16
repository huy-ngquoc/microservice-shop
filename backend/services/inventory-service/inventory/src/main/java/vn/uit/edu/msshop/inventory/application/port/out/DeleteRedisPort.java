package vn.uit.edu.msshop.inventory.application.port.out;

import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public interface DeleteRedisPort {
    public void delete(VariantId variantId);
}
