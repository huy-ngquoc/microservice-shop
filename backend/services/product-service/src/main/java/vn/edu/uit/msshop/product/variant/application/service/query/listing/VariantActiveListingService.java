package vn.edu.uit.msshop.product.variant.application.service.query.listing;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.query.listing.VariantActiveListingQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.listing.VariantActiveListingUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveListingPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantActiveListingService
        implements VariantActiveListingUseCase {

    private final VariantActiveListingPort activeListingPort;
    private final VariantSoldCountBulkLookupByVariantIdsPort soldCountBulkLookupByIdsPort;
    private final VariantStockCountBulkLookupByVariantIdsPort stockCountBulkLookupByIdsPort;

    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.VARIANT_LIST)
    public PageResponseDto<VariantView> list(
            final VariantActiveListingQuery query) {
        final var page = this.activeListingPort.listActive(query);

        final var ids = page.items().stream()
                .map(Variant::getId)
                .collect(Collectors.toUnmodifiableSet());

        final var soldCountById = soldCountBulkLookupByIdsPort.loadAllByVariantIds(ids);
        final var stockCountById = stockCountBulkLookupByIdsPort.loadAllByVariantIds(ids);

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
