package vn.edu.uit.msshop.product.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

public record CreateBrandRequest(
        @NotBlank
        @Size(
                max = BrandName.MAX_LENGTH)
        String name) {
}
