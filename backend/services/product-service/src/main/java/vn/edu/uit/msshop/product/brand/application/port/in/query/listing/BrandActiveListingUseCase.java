package vn.edu.uit.msshop.product.brand.application.port.in.query.listing;

import vn.edu.uit.msshop.product.brand.application.dto.query.listing.BrandActiveListingQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface BrandActiveListingUseCase {
    PageResponseDto<BrandView> list(
            final BrandActiveListingQuery query);
}
