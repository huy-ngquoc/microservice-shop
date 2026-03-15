package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

public interface FindBrandLogoUseCase {
    BrandLogoView findLogoById(
            final BrandId id);
}
