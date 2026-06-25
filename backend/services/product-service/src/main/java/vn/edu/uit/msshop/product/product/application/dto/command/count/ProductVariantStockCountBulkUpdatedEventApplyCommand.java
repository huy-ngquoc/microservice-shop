package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.List;
import java.util.UUID;

public record ProductVariantStockCountBulkUpdatedEventApplyCommand(
        UUID eventId,
        List<ProductVariantStockCountDelta> deltas) {

    public ProductVariantStockCountBulkUpdatedEventApplyCommand {
        deltas = List.copyOf(deltas);
    }

    public record ProductVariantStockCountDelta(
            UUID variantId,
            UUID productId,
            int delta) {
    }
}
