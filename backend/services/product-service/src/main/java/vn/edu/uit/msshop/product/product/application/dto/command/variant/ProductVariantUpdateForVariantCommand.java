package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.List;
import java.util.UUID;

public record ProductVariantUpdateForVariantCommand(
        UUID productId,
        UUID variantId,
        long variantPrice,
        List<String> variantTraitList) {

    public ProductVariantUpdateForVariantCommand {
        variantTraitList = List.copyOf(variantTraitList);
    }
}
