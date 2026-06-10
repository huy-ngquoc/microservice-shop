package vn.edu.uit.msshop.product.product.application.port.out.sync;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

public interface ProductVariantBulkRestorationByIdsPort {
    void restoreByVariantIds(
            Collection<ProductVariantId> variantIds);
}
