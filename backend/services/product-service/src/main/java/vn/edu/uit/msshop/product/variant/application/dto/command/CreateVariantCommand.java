package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

public record CreateVariantCommand(
        VariantProductId productId,
        VariantPrice price,
        VariantTraits traits) {
}
