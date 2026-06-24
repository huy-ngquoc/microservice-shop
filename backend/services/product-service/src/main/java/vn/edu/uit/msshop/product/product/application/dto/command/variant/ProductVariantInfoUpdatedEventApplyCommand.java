package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.List;
import java.util.UUID;

public record ProductVariantInfoUpdatedEventApplyCommand(
        UUID eventId,
        UUID productId,
        UUID variantId,
        long variantPrice,
        List<String> variantTraitList) {

    public ProductVariantInfoUpdatedEventApplyCommand {
        variantTraitList = List.copyOf(variantTraitList);
    }
}
