package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductStockCountValue;

public interface ProductStockCountValueSetByProductIdPort {
    void setByProductId(
            final ProductId productId,
            final ProductStockCountValue value);
}
