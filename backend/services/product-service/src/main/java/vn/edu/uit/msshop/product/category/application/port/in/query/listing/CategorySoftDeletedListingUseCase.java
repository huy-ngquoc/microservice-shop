package vn.edu.uit.msshop.product.category.application.port.in.query.listing;

import vn.edu.uit.msshop.product.category.application.dto.query.listing.CategorySoftDeletedListingQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface CategorySoftDeletedListingUseCase {
    PageResponseDto<CategoryView> list(
            final CategorySoftDeletedListingQuery query);
}
