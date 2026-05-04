package vn.edu.uit.msshop.product.brand.application.port.in.query;

import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface FindBrandLogoUseCase {
    BrandLogoView findLogoById(
            final BrandId id);
}
