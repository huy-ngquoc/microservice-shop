package vn.edu.uit.msshop.product.variant.application.dto.command;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record RemoveProductOptionCommand(
        ProductId id,
        int optionIndex,
        @Nullable
        ProductVariantPrice defaultPrice,
        ProductVersion expectedVersion) {
}
