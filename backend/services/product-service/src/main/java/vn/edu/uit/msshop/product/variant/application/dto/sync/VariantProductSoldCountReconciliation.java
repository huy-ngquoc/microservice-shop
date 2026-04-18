package vn.edu.uit.msshop.product.variant.application.dto.sync;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public record VariantProductSoldCountReconciliation(
        VariantProductId productId,
        int newSoldCount) {
}
