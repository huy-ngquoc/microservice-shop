package vn.edu.uit.msshop.product.product.application.port.out;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;

public interface SoftDeleteAllProductVariantsPort {
    void deleteByIds(
            final Collection<ProductVariantId> variantIds);
}
