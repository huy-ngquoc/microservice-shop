package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.List;
import java.util.UUID;

public record ProductVariantSoldCountBulkUpdatedEventApplyCommand(
        UUID eventId,
        List<ProductVariantSoldCountDelta> deltas) {

    public ProductVariantSoldCountBulkUpdatedEventApplyCommand {
        deltas = List.copyOf(deltas);
    }

    public record ProductVariantSoldCountDelta(
            UUID variantId,
            UUID productId,
            int delta) {
    }
}
