package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;

public record CreateProductRequest(
        @NotBlank
        String name,

        @NotNull
        UUID categoryId,

        @NotNull
        UUID brandId,

        @NotEmpty
        @Size(
                max = ProductOptions.MAX_AMOUNT)
        List<@NotBlank @Size(
                max = ProductOption.MAX_RAW_LENGTH_VALUE) String> options,

        @NotEmpty
        List<@Valid CreateProductVariantRequest> variants) {
    public CreateProductRequest {
        if (variants.size() > ProductVariants.MAX_AMOUNT) {
            throw new IllegalArgumentException("Too many variants");
        }
    }
}
