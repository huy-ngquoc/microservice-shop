package vn.edu.uit.msshop.product.variant.application.dto.command.image;

import java.util.UUID;

public record VariantImageDeletionByIdCommand(
        UUID variantId,
        long variantVersion) {
}
