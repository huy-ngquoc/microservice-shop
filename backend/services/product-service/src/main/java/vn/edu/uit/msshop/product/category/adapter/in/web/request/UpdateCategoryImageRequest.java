package vn.edu.uit.msshop.product.category.adapter.in.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateCategoryImageRequest(
        @Valid
        ChangeRequest<@NotBlank String> imageKey) {
}
