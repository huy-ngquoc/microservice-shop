package vn.edu.uit.msshop.product.variant.application.dto.query.lookup;

import java.util.Set;
import java.util.UUID;

public record VariantActiveBulkLookupByIdsQuery(
        Set<UUID> variantIdSet) {

    public VariantActiveBulkLookupByIdsQuery {
        variantIdSet = Set.copyOf(variantIdSet);
    }
}
