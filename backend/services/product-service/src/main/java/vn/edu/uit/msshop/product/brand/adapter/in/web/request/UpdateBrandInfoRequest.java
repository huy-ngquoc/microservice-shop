package vn.edu.uit.msshop.product.brand.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateBrandInfoRequest(
        ChangeRequest<@NotBlank @Size(
                max = BrandName.MAX_RAW_LENGTH) String> name,

        @NotNull
        Long version) {
}
