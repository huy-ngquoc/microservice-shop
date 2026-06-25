package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.UUID;

public record ProductVariantProjectionRebuildByProductIdCommand(
        UUID productId) {
}
