package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

public record RemoveProductVariantForVariantCommand(
        ProductId id,
        ProductVariantId variantId,
        int soldDecrement,
        int stockDecrement) {
    public RemoveProductVariantForVariantCommand {
        if (soldDecrement < 0) {
            throw new IllegalArgumentException("soldDecrement cannot be negative");
        }
        if (stockDecrement < 0) {
            throw new IllegalArgumentException("stockDecrement cannot be negative");
        }
    }
}
