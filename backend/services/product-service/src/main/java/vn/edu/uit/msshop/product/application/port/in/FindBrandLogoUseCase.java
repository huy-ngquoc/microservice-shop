package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

public interface FindBrandLogoUseCase {
    BrandLogoView findById(
            final BrandId id);
}
