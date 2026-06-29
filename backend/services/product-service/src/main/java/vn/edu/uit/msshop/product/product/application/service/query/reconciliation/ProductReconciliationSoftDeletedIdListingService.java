package vn.edu.uit.msshop.product.product.application.service.query.reconciliation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationSoftDeletedIdListingQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation.ProductReconciliationSoftDeletedIdListingUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductSoftDeletedListingPort;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ProductReconciliationSoftDeletedIdListingService
        implements ProductReconciliationSoftDeletedIdListingUseCase {

    private final ProductSoftDeletedListingPort softDeletedListingPort;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<UUID> list(
            final ProductReconciliationSoftDeletedIdListingQuery query) {
        final var pageRequest = query.pageRequest();
        return this.softDeletedListingPort.listSoftDeleted(pageRequest)
                .map(p -> p.getId().value());
    }
}
