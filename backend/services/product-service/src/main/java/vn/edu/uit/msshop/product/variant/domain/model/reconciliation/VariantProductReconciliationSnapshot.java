package vn.edu.uit.msshop.product.variant.domain.model.reconciliation;

import java.util.Set;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;

public record VariantProductReconciliationSnapshot(
        VariantProductId productId,
        VariantProductName name,
        Set<VariantId> variantIdSet) {

    public VariantProductReconciliationSnapshot {
        variantIdSet = Set.copyOf(variantIdSet);
    }
}
