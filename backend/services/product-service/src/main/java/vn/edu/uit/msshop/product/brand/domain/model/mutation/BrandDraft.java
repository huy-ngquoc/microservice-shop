package vn.edu.uit.msshop.product.brand.domain.model.mutation;

import vn.edu.uit.msshop.product.brand.domain.model.BrandName;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record BrandDraft(
        BrandName name) {
    public BrandDraft {
        if (name == null) {
            throw new DomainException("Name is null");
        }
    }
}
