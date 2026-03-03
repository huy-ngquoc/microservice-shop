package vn.edu.uit.msshop.product.brand.adapter.in.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateBrandInfoRequest(
        @Valid
        ChangeRequest<@NotBlank @Size(
                max = 50) String> name) {
}
