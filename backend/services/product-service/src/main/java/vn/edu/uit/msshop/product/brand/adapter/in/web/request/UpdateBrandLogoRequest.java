package vn.edu.uit.msshop.product.brand.adapter.in.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateBrandLogoRequest(
        @Valid
        ChangeRequest<@NotBlank @Size(
                max = BrandLogoKey.MAX_LENGTH) String> logoKey,

        @NotNull
        Long version) {
}
