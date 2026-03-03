package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

public interface FindBrandUseCase {
    BrandView findById(
            final BrandId id);
}
