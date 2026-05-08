package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;

public interface UpdateVariantStockCountPort {
    VariantStockCount update(
            final VariantStockCount stockCount);
}
