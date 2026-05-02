package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantInventoryStockCount;

public interface FetchInventoryStockCountsPort {
    List<VariantInventoryStockCount> fetchAll();
}
