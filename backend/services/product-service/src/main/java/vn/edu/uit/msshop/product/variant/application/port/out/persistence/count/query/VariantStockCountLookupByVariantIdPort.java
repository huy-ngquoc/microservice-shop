package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface VariantStockCountLookupByVariantIdPort {
    VariantStockCount loadByVariantIdOrZero(
            final VariantId variantId,
            final VariantProductId productId);
}
