package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductSoldCountValue;

public interface ProductSoldCountSetByProductIdPort {
    void setByProductId(
            final ProductId productId,
            final ProductSoldCountValue value);
}
