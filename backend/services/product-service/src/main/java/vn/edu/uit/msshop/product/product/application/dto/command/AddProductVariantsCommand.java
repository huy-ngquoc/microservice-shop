package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record AddProductVariantsCommand(
        ProductId productId,
        NewProductVariants newVariants,
        ProductVersion expectedVersion) {
}
