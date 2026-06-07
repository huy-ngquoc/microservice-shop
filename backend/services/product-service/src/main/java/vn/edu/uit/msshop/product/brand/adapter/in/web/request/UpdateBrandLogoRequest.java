package vn.edu.uit.msshop.product.brand.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryPublicIds;

public record UpdateBrandLogoRequest(
        @NotBlank
        @Size(
                max = BrandLogoKey.MAX_LENGTH + CloudinaryPublicIds.TEMP_PREFIX_LENGTH)
        String logoId,

        @NotNull
        Long version) {
}
