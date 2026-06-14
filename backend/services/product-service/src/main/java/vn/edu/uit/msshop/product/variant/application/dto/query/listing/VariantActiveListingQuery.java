package vn.edu.uit.msshop.product.variant.application.dto.query.listing;

import java.util.List;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

public record VariantActiveListingQuery(
        PageRequestDto pageRequest,
        List<String> targetList) {

    public VariantActiveListingQuery {
        targetList = List.copyOf(targetList);
    }
}
