package vn.edu.uit.msshop.product.adapter.in.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.adapter.in.web.request.common.ChangeRequest;

public record UpdateCategoryInfoRequest(
        @Valid
        ChangeRequest<@NotBlank @Size(
                max = 50) String> name) {
}
