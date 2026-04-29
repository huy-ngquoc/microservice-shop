package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record IncreaseProductSoldCountsForVariantCommand(
        Map<ProductId, Integer> incrementById) {
    public IncreaseProductSoldCountsForVariantCommand {
        incrementById = Map.copyOf(incrementById);
    }
}
