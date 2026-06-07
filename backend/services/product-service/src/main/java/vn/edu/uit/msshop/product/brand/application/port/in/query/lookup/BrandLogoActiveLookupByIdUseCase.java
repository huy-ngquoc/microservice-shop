package vn.edu.uit.msshop.product.brand.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.brand.application.dto.query.lookup.BrandLogoActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public interface BrandLogoActiveLookupByIdUseCase {
    BrandLogoView find(
            final BrandLogoActiveLookupByIdQuery query);
}
