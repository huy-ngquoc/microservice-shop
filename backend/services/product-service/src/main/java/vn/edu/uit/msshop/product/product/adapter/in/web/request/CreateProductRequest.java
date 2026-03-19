package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

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

        List<@NotBlank @Size(
                max = ProductOption.MAX_RAW_LENGTH_VALUE) String> options,

        List<@NotNull CreateProductVariantRequest> variants) {
    public CreateProductRequest {
        options = List.copyOf(options);
        variants = List.copyOf(variants);
    }
}
