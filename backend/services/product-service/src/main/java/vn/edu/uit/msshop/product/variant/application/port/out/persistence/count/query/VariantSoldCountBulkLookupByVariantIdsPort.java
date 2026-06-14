package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantSoldCountBulkLookupByVariantIdsPort {
    Map<VariantId, VariantSoldCount> loadAllByVariantIds(
            final Set<VariantId> variantIdSet);
}
