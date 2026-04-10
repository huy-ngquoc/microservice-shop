package vn.edu.uit.msshop.product.category.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;

public record UpdateCategoryImageRequest(
        @NotBlank
        @Size(
                max = CategoryImageKey.MAX_LENGTH)
        String newImageKey,

        @NotNull
        Long version) {
}
