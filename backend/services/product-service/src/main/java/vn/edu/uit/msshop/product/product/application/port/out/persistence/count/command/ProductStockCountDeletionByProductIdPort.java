package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductStockCountDeletionByProductIdPort {
    void deleteByProductId(
            final ProductId productId);
}
