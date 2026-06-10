package vn.edu.uit.msshop.product.brand.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.brand.application.dto.query.lookup.BrandSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface BrandSoftDeletedLookupByIdUseCase {
    BrandView find(
            final BrandSoftDeletedLookupByIdQuery query);
}
