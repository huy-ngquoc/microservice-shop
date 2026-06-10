package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.Map;
import java.util.UUID;

public record ProductSoldCountDecreaseForVariantsCommand(
        Map<UUID, Integer> decrementById) {
    public ProductSoldCountDecreaseForVariantsCommand {
        decrementById = Map.copyOf(decrementById);
    }
}
