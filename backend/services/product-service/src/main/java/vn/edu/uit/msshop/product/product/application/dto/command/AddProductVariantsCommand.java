package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVersion;

public record AddProductVariantsCommand(
        ProductId productId,
        NewProductVariants newVariants,
        ProductVersion expectedVersion) {
}
