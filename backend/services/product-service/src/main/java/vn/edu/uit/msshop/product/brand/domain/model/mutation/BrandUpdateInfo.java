package vn.edu.uit.msshop.product.brand.domain.model.mutation;

import lombok.Builder;
import vn.edu.uit.msshop.product.brand.domain.model.BrandName;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

@Builder
public record BrandUpdateInfo(
        BrandName name) {
    public BrandUpdateInfo {
        if (name == null) {
            throw new DomainException("Name is null");
        }
    }
}
