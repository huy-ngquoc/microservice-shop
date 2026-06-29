package vn.edu.uit.msshop.product.variant.application.service.query.listing;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.listing.VariantSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.listing.VariantSoftDeletedListingUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedListingPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
class VariantSoftDeletedListingService
        implements VariantSoftDeletedListingUseCase {

    private final VariantSoftDeletedListingPort softDeletedListingPort;
    private final VariantSoldCountBulkLookupByVariantIdsPort soldCountBulkLookupByIdsPort;
    private final VariantStockCountBulkLookupByVariantIdsPort stockCountBulkLookupByIdsPort;

    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<VariantView> list(
            final VariantSoftDeletedListingQuery query) {
        final var productId = VariantProductId.ofNullable(query.productId());
        final var page = this.softDeletedListingPort.listSoftDeleted(
                query.pageRequest(),
                productId);

        final var variantIdSet = page.items().stream()
                .map(Variant::getId)
                .collect(Collectors.toUnmodifiableSet());

        final var soldCountById = soldCountBulkLookupByIdsPort.loadAllByVariantIds(variantIdSet);
        final var stockCountById = stockCountBulkLookupByIdsPort.loadAllByVariantIds(variantIdSet);

        return page.map(v -> this.toView(
                v,
                soldCountById,
                stockCountById));
    }

    private VariantView toView(
            final Variant variant,
            final Map<VariantId, VariantSoldCount> soldCountById,
            final Map<VariantId, VariantStockCount> stockCountById) {
        final var variantId = variant.getId();
        final var productId = variant.getProductId();

        final var soldCount = soldCountById.getOrDefault(
                variantId,
                VariantSoldCount.zero(variantId, productId));
        final var stockCount = stockCountById.getOrDefault(
                variantId,
                VariantStockCount.zero(variantId, productId));

        return this.mapper.toView(
                variant,
                soldCount,
                stockCount);
    }
}
