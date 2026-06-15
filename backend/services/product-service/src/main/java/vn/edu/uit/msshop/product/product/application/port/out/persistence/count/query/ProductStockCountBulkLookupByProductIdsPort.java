package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductStockCountBulkLookupByProductIdsPort {
    Map<ProductId, ProductStockCount> loadAllByProductIds(
            final Set<ProductId> productIdSet);
}
