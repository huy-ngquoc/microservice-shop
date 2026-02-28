package vn.edu.uit.msshop.product.domain.model.brand.command;

import lombok.Builder;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

@Builder
public record BrandUpdateInfo(
        BrandName name) {
    public BrandUpdateInfo {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
    }
}
