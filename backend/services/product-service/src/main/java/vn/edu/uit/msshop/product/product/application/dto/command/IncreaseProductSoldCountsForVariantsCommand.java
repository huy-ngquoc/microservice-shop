package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record IncreaseProductSoldCountsForVariantsCommand(
        Map<ProductId, Integer> incrementById) {
    public IncreaseProductSoldCountsForVariantsCommand {
        incrementById = Map.copyOf(incrementById);
    }
}
