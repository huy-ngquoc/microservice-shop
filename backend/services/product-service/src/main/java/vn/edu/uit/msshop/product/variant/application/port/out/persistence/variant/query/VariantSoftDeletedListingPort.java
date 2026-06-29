package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface VariantSoftDeletedListingPort {
    PageResponseDto<Variant> listSoftDeleted(
            final PageRequestDto pageRequest,

            @Nullable
            final VariantProductId productId);
}
