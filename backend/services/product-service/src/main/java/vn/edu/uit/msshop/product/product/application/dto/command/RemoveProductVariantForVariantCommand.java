package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;

public record RemoveProductVariantForVariantCommand(
        ProductId id,
        ProductVariantId variantId) {
}
