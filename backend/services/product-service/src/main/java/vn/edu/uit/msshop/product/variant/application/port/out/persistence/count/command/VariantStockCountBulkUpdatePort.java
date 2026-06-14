package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;

public interface VariantStockCountBulkUpdatePort {
    void updateAll(
            final Collection<VariantStockCount> stockCountCollection);
}
