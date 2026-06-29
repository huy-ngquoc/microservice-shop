package vn.edu.uit.msshop.product.variant.application.service.query.count;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantSoldCountValueSumByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.port.in.query.count.VariantSoldCountValueSumByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
class VariantSoldCountValueSumByProductIdService
        implements VariantSoldCountValueSumByProductIdUseCase {

    private final VariantActiveBulkLookupByProductIdPort activeBulkLookupByProductIdPort;
    private final VariantSoldCountBulkLookupByVariantIdsPort soldCountBulkLookupByVariantIdsPort;

    @Override
    @Transactional(
            readOnly = true)
    public int sum(
            final VariantSoldCountValueSumByProductIdQuery query) {
        final var productId = new VariantProductId(query.productId());
        final var activeIdSet = this.activeBulkLookupByProductIdPort.loadAllActiveByProductId(productId)
                .stream()
                .map(Variant::getId)
                .collect(Collectors.toUnmodifiableSet());
        if (activeIdSet.isEmpty()) {
            return 0;
        }

        final var soldCountByVariantId = this.soldCountBulkLookupByVariantIdsPort.loadAllByVariantIds(activeIdSet);
        return soldCountByVariantId.values().stream()
                .mapToInt(soldCount -> soldCount.getValue().value())
                .sum();
    }
}
