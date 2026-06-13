package vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle;

import java.util.UUID;

public record VariantRestorationByIdCommand(
        UUID variantId,
        long variantVersion) {
}
