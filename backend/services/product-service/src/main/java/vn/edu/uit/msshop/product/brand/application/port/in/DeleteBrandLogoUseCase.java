package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

public interface DeleteBrandLogoUseCase {
    void deleteById(
            final BrandId id);
}
