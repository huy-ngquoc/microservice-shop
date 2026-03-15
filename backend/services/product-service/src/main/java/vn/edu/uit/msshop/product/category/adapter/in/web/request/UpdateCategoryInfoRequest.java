package vn.edu.uit.msshop.product.category.adapter.in.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

public record UpdateCategoryInfoRequest(
        @Valid
        ChangeRequest<@NotBlank @Size(
                max = CategoryName.MAX_LENGTH) String> name,

        long version) {
}
