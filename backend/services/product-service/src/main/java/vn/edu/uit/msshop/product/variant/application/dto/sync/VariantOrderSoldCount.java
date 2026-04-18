package vn.edu.uit.msshop.product.variant.application.dto.sync;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public record VariantOrderSoldCount(
        VariantId variantId,
        int soldCount) {
}
