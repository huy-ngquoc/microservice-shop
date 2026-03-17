package vn.edu.uit.msshop.product.variant.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;

public record UpdateVariantImageRequest(
        @NotBlank
        @Size(
                max = VariantImageKey.MAX_LENGTH)
        String newImageKey,

        @NotBlank
        Long version) {
}
