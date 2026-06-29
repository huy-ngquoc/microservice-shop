package vn.edu.uit.msshop.product.product.application.port.out.sync;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductVariantStockCountValueSumByProductIdPort {
    int sumStockByProductId(
            final ProductId productId);
}
