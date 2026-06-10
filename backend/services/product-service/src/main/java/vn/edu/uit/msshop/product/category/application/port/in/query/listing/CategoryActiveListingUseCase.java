package vn.edu.uit.msshop.product.category.application.port.in.query.listing;

import vn.edu.uit.msshop.product.category.application.dto.query.listing.CategoryActiveListingQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface CategoryActiveListingUseCase {
    PageResponseDto<CategoryView> list(
            final CategoryActiveListingQuery query);
}
