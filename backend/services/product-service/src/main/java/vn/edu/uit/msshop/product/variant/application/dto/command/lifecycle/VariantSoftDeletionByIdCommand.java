package vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle;

import java.util.UUID;

public record VariantSoftDeletionByIdCommand(
        UUID variantId,
        long variantVersion) {
}
