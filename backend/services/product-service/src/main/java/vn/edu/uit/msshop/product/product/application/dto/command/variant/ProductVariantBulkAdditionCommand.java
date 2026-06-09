package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record ProductVariantBulkAdditionCommand(
        ProductId id,
        NewProductVariants newVariants,
        ProductVersion expectedVersion) {
}
