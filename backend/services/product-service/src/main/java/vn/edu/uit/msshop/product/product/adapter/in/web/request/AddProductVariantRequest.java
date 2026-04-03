package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;

public record AddProductVariantRequest(
        @NotNull
        @PositiveOrZero
        Long price,

        @NotEmpty
        List<@NotBlank @Size(
                max = ProductVariantTrait.MAX_RAW_LENGTH) String> traits,

        @NotNull
        Long expectedVersion) {
}
