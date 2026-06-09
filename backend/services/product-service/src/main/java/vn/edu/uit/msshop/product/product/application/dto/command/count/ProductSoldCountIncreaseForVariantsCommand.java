package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record ProductSoldCountIncreaseForVariantsCommand(
        Map<ProductId, Integer> incrementById) {
    public ProductSoldCountIncreaseForVariantsCommand {
        incrementById = Map.copyOf(incrementById);
    }
}
