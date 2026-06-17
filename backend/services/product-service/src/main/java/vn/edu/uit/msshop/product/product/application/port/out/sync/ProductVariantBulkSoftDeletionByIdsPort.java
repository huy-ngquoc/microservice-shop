package vn.edu.uit.msshop.product.product.application.port.out.sync;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

public interface ProductVariantBulkSoftDeletionByIdsPort {
    void deleteByIds(
            Collection<ProductVariantId> variantIdCollection,
            ProductId productId);
}
