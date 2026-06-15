package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantSoftDeletedBulkLookupByIdsPort {
    Map<VariantId, Variant> loadAllSoftDeletedByIds(
            final Set<VariantId> idSet);
}
