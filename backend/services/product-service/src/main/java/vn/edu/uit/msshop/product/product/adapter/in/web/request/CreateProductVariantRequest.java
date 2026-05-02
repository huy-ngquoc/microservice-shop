package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTarget;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;

public record CreateProductVariantRequest(
        @NotNull
        @PositiveOrZero
        Long price,

        @NotEmpty
        @Size(
                max = ProductVariantTraits.MAX_TRAITS_AMOUNT)
        List<@NotBlank @Size(
                max = ProductVariantTrait.MAX_RAW_LENGTH) String> traits,

        @NotEmpty
        @Size(
                max = ProductVariantTargets.MAX_AMOUNT)
        List<@NotBlank @Size(
                max = ProductVariantTarget.MAX_RAW_LENGTH) String> targets) {
}
