package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.List;
import java.util.UUID;

public record ProductVariantRestoredEventApplyCommand(
        UUID eventId,
        UUID productId,
        UUID variantId,
        long variantPrice,
        List<String> variantTraitList,
        int productSoldCountIncrement,
        int productStockCountIncrement) {

    public ProductVariantRestoredEventApplyCommand {
        variantTraitList = List.copyOf(variantTraitList);
    }
}
