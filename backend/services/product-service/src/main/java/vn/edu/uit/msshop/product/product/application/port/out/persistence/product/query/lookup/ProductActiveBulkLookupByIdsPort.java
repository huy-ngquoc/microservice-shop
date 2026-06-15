package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductActiveBulkLookupByIdsPort {
    Map<ProductId, Product> loadAllByIds(
            final Set<ProductId> idSet);
}
