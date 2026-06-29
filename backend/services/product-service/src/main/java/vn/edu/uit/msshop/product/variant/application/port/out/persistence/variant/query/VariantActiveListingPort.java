package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;

public interface VariantActiveListingPort {
    PageResponseDto<Variant> listActive(
            final PageRequestDto pageRequest,
            final VariantTargets targets);
}
