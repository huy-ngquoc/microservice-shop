package vn.edu.uit.msshop.product.variant.application.dto.command.sync;

import java.util.UUID;

public record VariantBulkHardDeletionByProductIdForProductCommand(
        UUID productId) {
}
