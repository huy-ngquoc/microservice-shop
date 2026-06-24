package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.UUID;

public record ProductVariantSoftDeletedEventApplyCommand(
        UUID eventId,
        UUID productId,
        UUID variantId,
        int productSoldCountDecrement,
        int productStockCountDecrement) {
}
