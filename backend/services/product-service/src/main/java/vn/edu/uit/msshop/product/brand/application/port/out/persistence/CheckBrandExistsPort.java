package vn.edu.uit.msshop.product.brand.application.port.out.persistence;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface CheckBrandExistsPort {
    boolean existsById(
            final BrandId id);
}
