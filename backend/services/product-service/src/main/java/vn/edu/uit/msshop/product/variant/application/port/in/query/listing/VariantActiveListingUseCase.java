package vn.edu.uit.msshop.product.variant.application.port.in.query.listing;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface VariantActiveListingUseCase {
    PageResponseDto<VariantView> list(
            final ListVariantsQuery query);
}
