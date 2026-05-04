package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record DecreaseProductSoldCountsForVariantsCommand(
        Map<ProductId, Integer> decrementById) {
    public DecreaseProductSoldCountsForVariantsCommand {
        decrementById = Map.copyOf(decrementById);
    }
}
