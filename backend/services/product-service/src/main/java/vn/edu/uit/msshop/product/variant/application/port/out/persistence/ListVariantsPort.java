package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.application.dto.query.listing.VariantActiveListingQuery;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface ListVariantsPort {
    PageResponseDto<Variant> list(
            VariantActiveListingQuery query);
}
