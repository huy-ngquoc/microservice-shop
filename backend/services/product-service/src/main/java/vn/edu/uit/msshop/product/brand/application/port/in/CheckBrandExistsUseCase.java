package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

public interface CheckBrandExistsUseCase {
    boolean existsById(
            final BrandId id);
}
