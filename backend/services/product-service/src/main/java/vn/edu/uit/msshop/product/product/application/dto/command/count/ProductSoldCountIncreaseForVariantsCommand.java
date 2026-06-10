package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.Map;
import java.util.UUID;

public record ProductSoldCountIncreaseForVariantsCommand(
        Map<UUID, Integer> incrementById) {
    public ProductSoldCountIncreaseForVariantsCommand {
        incrementById = Map.copyOf(incrementById);
    }
}
