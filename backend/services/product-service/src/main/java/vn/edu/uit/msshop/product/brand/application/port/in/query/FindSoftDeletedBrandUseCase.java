package vn.edu.uit.msshop.product.brand.application.port.in.query;

import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface FindSoftDeletedBrandUseCase {
    BrandView findSoftDeletedById(
            final BrandId id);
}
