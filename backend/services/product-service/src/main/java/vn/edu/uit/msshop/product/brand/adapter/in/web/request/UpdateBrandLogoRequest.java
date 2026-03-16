package vn.edu.uit.msshop.product.brand.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;

public record UpdateBrandLogoRequest(
        @NotBlank
        @Size(
                max = BrandLogoKey.MAX_LENGTH)
        String newLogoKey,

        @NotNull
        Long version) {
}
