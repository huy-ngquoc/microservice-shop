package vn.edu.uit.msshop.product.variant.application.dto.sync;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

public record VariantInventoryStockCount(
        VariantId variantId,
        VariantStockCountValue value) {
}
