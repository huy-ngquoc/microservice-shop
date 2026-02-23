package vn.edu.uit.msshop.product.domain.model.brand.command;

import org.jspecify.annotations.NonNull;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

public record BrandUpdate(
        @NonNull
        BrandName name,

        BrandLogo logo) {
    public BrandUpdate {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
    }
}
