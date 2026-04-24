package vn.edu.uit.msshop.product.variant.application.dto.sync;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

public record VariantOrderSoldCount(
        VariantId variantId,
        VariantSoldCountValue value) {
}
