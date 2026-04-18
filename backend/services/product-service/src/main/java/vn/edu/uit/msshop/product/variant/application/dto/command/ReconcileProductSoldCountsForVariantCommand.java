package vn.edu.uit.msshop.product.variant.application.dto.command;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record ReconcileProductSoldCountsForVariantCommand(
        Collection<Item> items) {
    public record Item(
            ProductId productId,
            int newSoldCount) {
    }

    public ReconcileProductSoldCountsForVariantCommand {
        items = List.copyOf(items);
    }
}
