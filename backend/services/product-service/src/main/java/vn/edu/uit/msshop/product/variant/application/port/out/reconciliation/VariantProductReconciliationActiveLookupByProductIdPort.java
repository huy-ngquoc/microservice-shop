package vn.edu.uit.msshop.product.variant.application.port.out.reconciliation;

import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.reconciliation.VariantProductReconciliationSnapshot;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface VariantProductReconciliationActiveLookupByProductIdPort {
    Optional<VariantProductReconciliationSnapshot> findActiveByProductId(
            VariantProductId productId);
}
