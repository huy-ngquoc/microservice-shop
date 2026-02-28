package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

public interface DeleteBrandLogoUseCase {
    void deleteById(
            final BrandId id);
}
