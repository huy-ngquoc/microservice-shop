package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record ProductStockCountDecreaseForVariantsCommand(
        Map<ProductId, Integer> decrementById) {
    public ProductStockCountDecreaseForVariantsCommand {
        decrementById = Map.copyOf(decrementById);
    }
}
