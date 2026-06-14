package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query;

import java.util.Collection;
import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantStockCountBulkLookupByVariantIdsPort {
    Map<VariantId, VariantStockCount> loadAllByVariantIds(
            final Collection<VariantId> variantIdCollection);
}
