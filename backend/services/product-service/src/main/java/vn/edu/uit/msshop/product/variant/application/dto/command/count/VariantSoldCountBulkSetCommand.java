package vn.edu.uit.msshop.product.variant.application.dto.command.count;

import java.util.Map;
import java.util.UUID;

public record VariantSoldCountBulkSetCommand(
        Map<UUID, Integer> soldCountByVariantId) {

    public VariantSoldCountBulkSetCommand {
        soldCountByVariantId = Map.copyOf(soldCountByVariantId);
    }
}
