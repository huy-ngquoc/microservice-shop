package vn.edu.uit.msshop.product.variant.application.service.query;

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
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.ListVariantsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.ListVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class ListVariantsService
        implements ListVariantsUseCase {

    private static final Collector<VariantId, ?, Set<VariantId>> SET_COLLECTOR = Collectors.toSet();

    private final ListVariantsPort listPort;
    private final LoadAllVariantSoldCountsPort loadAllSoldCountsPort;
    private final LoadAllVariantStockCountsPort loadAllStockCountsPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.VARIANT_LIST)
    public PageResponseDto<VariantView> list(
            final ListVariantsQuery query) {
        final var page = this.listPort.list(query);

        final var ids = page.items().stream()
                .map(Variant::getId)
                .collect(ListVariantsService.SET_COLLECTOR);

        final var soldCountById = loadAllSoldCountsPort.loadAllByIds(ids);
        final var stockCountById = loadAllStockCountsPort.loadAllByIds(ids);

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
                VariantSoldCount.zero(
                        variantId,
                        productId));
        final var stockCount = stockCountById.getOrDefault(
                variantId,
                VariantStockCount.zero(
                        variantId,
                        productId));

        return this.mapper.toView(
                variant,
                soldCount,
                stockCount);
    }
}
