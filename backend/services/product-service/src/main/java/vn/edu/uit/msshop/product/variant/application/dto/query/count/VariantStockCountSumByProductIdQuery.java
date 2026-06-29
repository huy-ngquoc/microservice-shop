package vn.edu.uit.msshop.product.variant.application.dto.query.count;

import java.util.UUID;

public record VariantStockCountSumByProductIdQuery(
        UUID productId) {
}
