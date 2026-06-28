package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.UUID;

public record ProductVariantSoftDeletedForProductEventApplyCommand(
        UUID productId,
        int soldCountDecrement,
        int stockCountDecrement) {
}
