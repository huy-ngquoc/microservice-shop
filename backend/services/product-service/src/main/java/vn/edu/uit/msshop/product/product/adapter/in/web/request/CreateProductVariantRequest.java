package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTrait;

public record CreateProductVariantRequest(
        long price,

        List<@NotBlank @Size(
                max = ProductVariantTrait.MAX_RAW_LENGTH) String> traits) {
    public CreateProductVariantRequest {
        if (traits == null) {
            traits = List.of();
        } else {
            traits = List.copyOf(traits);
        }
    }
}
