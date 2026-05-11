package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTarget;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;

public record CreateSimpleProductRequest(
        @NotBlank
        @Size(
                max = ProductName.MAX_LENGTH)
        String name,

        @NotNull
        UUID categoryId,

        @NotNull
        UUID brandId,

        Long price,

        @NotEmpty
        @Size(
                max = ProductVariantTargets.MAX_AMOUNT)
        List<
                @NotBlank @Size(
                        max = ProductVariantTarget.MAX_RAW_LENGTH) String> targets) {
}
