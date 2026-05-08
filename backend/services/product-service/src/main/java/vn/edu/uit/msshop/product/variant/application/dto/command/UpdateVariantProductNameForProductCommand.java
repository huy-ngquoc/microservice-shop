package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;

public record UpdateVariantProductNameForProductCommand(
        VariantProductId productId,
        VariantProductName productName) {
}
