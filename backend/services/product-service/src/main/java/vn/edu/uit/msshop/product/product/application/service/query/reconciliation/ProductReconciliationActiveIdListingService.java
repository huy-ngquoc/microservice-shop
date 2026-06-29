package vn.edu.uit.msshop.product.product.application.service.query.reconciliation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationActiveIdListingQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation.ProductReconciliationActiveIdListingUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductActiveListingPort;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ProductReconciliationActiveIdListingService
        implements ProductReconciliationActiveIdListingUseCase {

    private final ProductActiveListingPort activeListingPort;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<UUID> list(
            final ProductReconciliationActiveIdListingQuery query) {
        final var pageRequest = query.pageRequest();
        return this.activeListingPort.list(pageRequest)
                .map(p -> p.getId().value());
    }
}
