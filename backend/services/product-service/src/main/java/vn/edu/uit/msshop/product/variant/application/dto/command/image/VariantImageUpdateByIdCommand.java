package vn.edu.uit.msshop.product.variant.application.dto.command.image;

import java.util.UUID;

public record VariantImageUpdateByIdCommand(
        UUID variantId,
        String newImageKey,
        long variantVersion) {
}
