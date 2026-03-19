package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTraits;

public record CreateProductVariantCommand(
        ProductVariantPrice price,
        ProductVariantTraits traits) {
}
