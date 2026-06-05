package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query;

import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductStockCountLookupByIdPort {
    ProductStockCount loadByIdOrZero(
            final ProductId id);
}
