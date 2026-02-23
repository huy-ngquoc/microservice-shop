package vn.edu.uit.msshop.product.domain.model.brand.command;

import org.jspecify.annotations.NonNull;

import lombok.Builder;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

@Builder
public record BrandDraft(
        @NonNull
        BrandName name,

        BrandLogo logo) {
    public BrandDraft {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
    }
}
