package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query;

import java.util.Collection;
import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductStockCountBulkLookupByProductIdsPort {
    Map<ProductId, ProductStockCount> loadAllByProductIds(
            final Collection<ProductId> productIdCollection);
}
