package vn.edu.uit.msshop.product.product.application.dto.command.count;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record ProductSoldCountDecreaseForVariantsCommand(
        Map<ProductId, Integer> decrementById) {
    public ProductSoldCountDecreaseForVariantsCommand {
        decrementById = Map.copyOf(decrementById);
    }
}
