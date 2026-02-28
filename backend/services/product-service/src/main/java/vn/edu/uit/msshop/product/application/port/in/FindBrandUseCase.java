package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

public interface FindBrandUseCase {
    BrandView findById(
            final BrandId id);
}
