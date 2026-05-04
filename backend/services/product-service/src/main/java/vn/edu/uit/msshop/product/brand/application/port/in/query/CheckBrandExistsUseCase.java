package vn.edu.uit.msshop.product.brand.application.port.in.query;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface CheckBrandExistsUseCase {
    boolean existsById(
            final BrandId id);
}
