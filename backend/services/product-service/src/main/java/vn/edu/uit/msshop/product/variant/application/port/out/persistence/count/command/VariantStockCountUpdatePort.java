package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;

public interface VariantStockCountUpdatePort {
    VariantStockCount update(
            final VariantStockCount stockCount);
}
