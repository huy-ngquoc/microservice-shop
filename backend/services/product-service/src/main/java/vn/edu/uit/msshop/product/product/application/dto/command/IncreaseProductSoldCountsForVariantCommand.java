package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record IncreaseProductSoldCountsForVariantCommand(
        Collection<Item> items) {
    public record Item(
            ProductId productId,
            int soldCountIncrement) {
    }

    public IncreaseProductSoldCountsForVariantCommand {
        items = List.copyOf(items);
    }
}
