package vn.edu.uit.msshop.product.variant.application.dto.command.count;

import java.util.Map;
import java.util.UUID;

public record VariantStockCountBulkSetCommand(
        Map<UUID, Integer> stockCountByVariantId) {

    public VariantStockCountBulkSetCommand {
        stockCountByVariantId = Map.copyOf(stockCountByVariantId);
    }
}
