package vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface BrandDeletionByIdPort {
    void deleteById(
            final BrandId id);
}
