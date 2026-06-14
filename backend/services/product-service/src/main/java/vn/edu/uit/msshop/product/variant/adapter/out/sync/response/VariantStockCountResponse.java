package vn.edu.uit.msshop.product.variant.adapter.out.sync.response;

import java.util.UUID;

public record VariantStockCountResponse(
        UUID variantId,
        int quantity) {
}
