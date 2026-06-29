package vn.edu.uit.msshop.product.product.application.dto.view;

import java.util.Set;
import java.util.UUID;

public record ProductReconciliationView(
        UUID productId,
        String name,
        Set<UUID> variantIdSet) {

    public ProductReconciliationView {
        variantIdSet = Set.copyOf(variantIdSet);
    }
}
