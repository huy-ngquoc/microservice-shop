package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.ProductOption;

public record CreateProductRequest(
        @NotBlank
        String name,

        @NotNull
        UUID categoryId,

        @NotNull
        UUID brandId,

        @Nullable
        Long price,

        List<@NotBlank @Size(
                max = ProductOption.MAX_RAW_LENGTH_VALUE) String> options,

        List<@Valid CreateProductVariantRequest> variants) {
    public CreateProductRequest {
        if (options != null) {
            options = List.copyOf(options);
        } else {
            options = List.of();
        }

        if (variants != null) {
            variants = List.copyOf(variants);
        } else {
            variants = List.of();
        }
    }

    @AssertTrue(
            message = "Price is required when no options and no variants are provided")
    public boolean isPriceValidForProductType() {
        return !options.isEmpty() || !variants.isEmpty() || (price != null);
    }
}
