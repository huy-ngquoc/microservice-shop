package vn.edu.uit.msshop.product.category.adapter.in.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateCategoryImageRequest(
        @Valid
        ChangeRequest<@NotBlank @Size(
                max = CategoryImageKey.MAX_LENGTH) String> imageKey,

        @NotNull
        Long version) {
}
