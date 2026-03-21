package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.NewVariantsForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

public record CreateVariantsForNewProductCommand(
        VariantProductId productId,
        NewVariantsForNewProduct newVariantsForNewProduct) {
}
