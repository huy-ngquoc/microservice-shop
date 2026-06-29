package vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation;

import java.util.UUID;

import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationActiveIdListingQuery;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface ProductReconciliationActiveIdListingUseCase {
    PageResponseDto<UUID> list(
            final ProductReconciliationActiveIdListingQuery query);
}
