package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;

public interface UpdateAllVariantStockCountsPort {
    void updateAll(
            final Collection<VariantStockCount> stockCounts);
}
