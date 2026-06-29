package vn.edu.uit.msshop.product.variant.application.service.query.count;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantStockCountSumByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.port.in.query.count.VariantStockCountSumByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
class VariantStockCountSumByProductIdService
        implements VariantStockCountSumByProductIdUseCase {

    private final VariantActiveBulkLookupByProductIdPort activeBulkLookupByProductIdPort;
    private final VariantStockCountBulkLookupByVariantIdsPort stockCountBulkLookupByVariantIdsPort;

    @Override
    public int sum(
            VariantStockCountSumByProductIdQuery query) {
        final var productId = new VariantProductId(query.productId());
        final var activeIdSet = this.activeBulkLookupByProductIdPort.loadAllActiveByProductId(productId)
                .stream()
                .map(Variant::getId)
                .collect(Collectors.toUnmodifiableSet());
        if (activeIdSet.isEmpty()) {
            return 0;
        }

        final var stockCountByVariantId = this.stockCountBulkLookupByVariantIdsPort.loadAllByVariantIds(activeIdSet);
        return stockCountByVariantId.values().stream()
                .mapToInt(soldCount -> soldCount.getValue().value())
                .sum();
    }

}
