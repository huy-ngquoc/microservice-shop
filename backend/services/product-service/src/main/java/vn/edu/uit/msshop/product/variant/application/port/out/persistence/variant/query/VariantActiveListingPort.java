package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.application.dto.query.listing.VariantActiveListingQuery;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface VariantActiveListingPort {
    PageResponseDto<Variant> listActive(
            VariantActiveListingQuery query);
}
