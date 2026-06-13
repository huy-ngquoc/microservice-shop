package vn.edu.uit.msshop.product.variant.application.service.query.lookup;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantsNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantActiveBulkLookupByIdsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantActiveBulkLookupByIdsService
        implements VariantActiveBulkLookupByIdsUseCase {

    private final LoadAllVariantsPort loadAllPort;
    private final LoadAllVariantSoldCountsPort loadAllSoldCountsPort;
    private final LoadAllVariantStockCountsPort loadAllStockCountsPort;
    private final VariantViewMapper mapper;

    @Override
    public Map<UUID, VariantView> findAllByIds(
            final Set<UUID> idSet) {
        if (idSet.isEmpty()) {
            return Map.of();
        }

        final var variantIdSet = idSet.stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());

        final var variantById = this.loadAllPort.loadAllByIds(variantIdSet);
        final var missing = variantIdSet.stream()
                .filter(id -> !variantById.containsKey(id))
                .collect(Collectors.toUnmodifiableSet());
        if (!missing.isEmpty()) {
            throw new VariantsNotFoundException(missing);
        }

        final var soldCountById = this.loadAllSoldCountsPort.loadAllByIds(variantIdSet);
        final var stockCountById = this.loadAllStockCountsPort.loadAllByIds(variantIdSet);

        final var viewById = HashMap.<UUID, VariantView>newHashMap(variantById.size());
        for (final var variant : variantById.values()) {
            final var rawVariantId = variant.getId().value();
            final var view = this.toView(
                    variant,
                    soldCountById,
                    stockCountById);

            viewById.put(rawVariantId, view);
        }
        return Map.copyOf(viewById);
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
