package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadProductStockCountPort {
    ProductStockCount loadByIdOrZero(
            final ProductId id);
}
