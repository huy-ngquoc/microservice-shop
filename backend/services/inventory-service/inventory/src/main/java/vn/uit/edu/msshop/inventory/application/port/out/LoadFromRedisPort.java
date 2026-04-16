package vn.uit.edu.msshop.inventory.application.port.out;

import java.util.List;

import vn.uit.edu.msshop.inventory.domain.model.Inventory;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public interface LoadFromRedisPort {
    public List<Inventory> loadFromRedis(List<VariantId> variantIds);
    public Inventory loadById(VariantId variantId);
}
