package vn.edu.uit.msshop.product.variant.application.port.out.reconciliation;

import java.util.UUID;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface VariantProductReconciliationSoftDeletedIdListingPort {
    PageResponseDto<UUID> listSoftDeletedIds(
            final PageRequestDto pageRequest);
}
