package vn.edu.uit.msshop.product.brand.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.brand.application.dto.query.lookup.BrandActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface BrandActiveLookupByIdUseCase {
    BrandView find(
            final BrandActiveLookupByIdQuery query);
}
