package vn.edu.uit.msshop.product.variant.application.port.in.query.listing;

import vn.edu.uit.msshop.product.variant.application.dto.query.listing.VariantSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface VariantSoftDeletedListingUseCase {
    PageResponseDto<VariantView> list(
            final VariantSoftDeletedListingQuery query);
}
