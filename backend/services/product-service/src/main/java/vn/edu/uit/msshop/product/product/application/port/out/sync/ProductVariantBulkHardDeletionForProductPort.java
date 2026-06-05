package vn.edu.uit.msshop.product.product.application.port.out.sync;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductVariantBulkHardDeletionForProductPort {
    void purgeByProductId(
            final ProductId productId);
}
