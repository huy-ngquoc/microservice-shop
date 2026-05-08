package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;

public record UpdateProductOptionRequest(
        @NotBlank
        @Size(
                max = ProductOption.MAX_RAW_LENGTH_VALUE)
        String option,

        @NotNull
        Long expectedVersion) {
}
