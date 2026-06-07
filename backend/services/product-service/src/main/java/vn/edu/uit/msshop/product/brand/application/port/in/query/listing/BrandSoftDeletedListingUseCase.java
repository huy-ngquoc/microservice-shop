package vn.edu.uit.msshop.product.brand.application.port.in.query.listing;

import vn.edu.uit.msshop.product.brand.application.dto.query.listing.BrandSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface BrandSoftDeletedListingUseCase {
    PageResponseDto<BrandView> list(
            final BrandSoftDeletedListingQuery query);
}
