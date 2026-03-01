package vn.edu.uit.msshop.product.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

public record CreateCategoryRequest(
        @NotBlank
        @Size(
                max = CategoryName.MAX_LENGTH)
        String name) {
}
