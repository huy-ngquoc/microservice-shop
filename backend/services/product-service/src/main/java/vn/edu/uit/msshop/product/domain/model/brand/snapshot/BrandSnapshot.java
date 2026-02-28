package vn.edu.uit.msshop.product.domain.model.brand.snapshot;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

public record BrandSnapshot(
        BrandId id,

        BrandName name,

        @Nullable
        BrandLogo logo) {
    public BrandSnapshot {
        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
    }
}
