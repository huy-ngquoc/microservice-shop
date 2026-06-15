package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query;

import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductSoldCountLookupByProductIdPort {
    ProductSoldCount loadByProductIdOrZero(
            final ProductId productId);
}
