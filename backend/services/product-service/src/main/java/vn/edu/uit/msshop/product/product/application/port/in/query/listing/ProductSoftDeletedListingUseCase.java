package vn.edu.uit.msshop.product.product.application.port.in.query.listing;

import vn.edu.uit.msshop.product.product.application.dto.query.listing.ProductSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface ProductSoftDeletedListingUseCase {
    PageResponseDto<ProductView> list(
            final ProductSoftDeletedListingQuery query);
}
