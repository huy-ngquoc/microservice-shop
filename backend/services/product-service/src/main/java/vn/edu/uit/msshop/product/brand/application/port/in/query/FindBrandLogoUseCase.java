package vn.edu.uit.msshop.product.brand.application.port.in.query;

import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface FindBrandLogoUseCase {
    BrandLogoView findLogoById(
            final BrandId id);
}
