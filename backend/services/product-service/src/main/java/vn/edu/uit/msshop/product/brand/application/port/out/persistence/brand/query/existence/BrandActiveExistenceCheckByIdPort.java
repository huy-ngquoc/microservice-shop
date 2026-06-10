package vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.existence;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface BrandActiveExistenceCheckByIdPort {
    boolean existsById(
            final BrandId id);
}
