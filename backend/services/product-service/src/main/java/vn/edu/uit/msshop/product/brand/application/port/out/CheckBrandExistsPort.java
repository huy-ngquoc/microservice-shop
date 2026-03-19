package vn.edu.uit.msshop.product.brand.application.port.out;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

public interface CheckBrandExistsPort {
    boolean existsById(
            final BrandId id);
}
