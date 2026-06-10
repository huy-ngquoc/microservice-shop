package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.Map;
import java.util.UUID;

public record ProductStockCountDecreaseForVariantsCommand(
        Map<UUID, Integer> decrementById) {
    public ProductStockCountDecreaseForVariantsCommand {
        decrementById = Map.copyOf(decrementById);
    }
}
