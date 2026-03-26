package vn.edu.uit.msshop.product.category.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;

public record CreateCategoryRequest(
        @NotBlank
        @Size(
                max = CategoryName.MAX_LENGTH)
        String name) {
}
