package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

public record CreateVariantCommand(
        VariantProductId productId,
        VariantPrice price,
        VariantTraits traits) {
}
