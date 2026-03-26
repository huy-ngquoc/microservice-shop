package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;

public record UpdateProductVariantForVariantCommand(
        ProductId id,
        ProductVariant updatedVariant) {
}
