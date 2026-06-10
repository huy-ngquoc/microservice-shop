package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.Map;
import java.util.UUID;

public record ProductStockCountIncreaseForVariantsCommand(
        Map<UUID, Integer> incrementById) {
    public ProductStockCountIncreaseForVariantsCommand {
        incrementById = Map.copyOf(incrementById);
    }
}
