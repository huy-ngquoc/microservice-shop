package vn.edu.uit.msshop.product.variant.application.service.query;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantsNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.FindAllVariantsByIdsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class FindAllVariantsByIdsService implements FindAllVariantsByIdsUseCase {
  private final LoadAllVariantsPort loadAllPort;
  private final LoadAllVariantSoldCountsPort loadAllSoldCountsPort;
  private final LoadAllVariantStockCountsPort loadAllStockCountsPort;
  private final VariantViewMapper mapper;

  @Override
  public Map<VariantId, VariantView> findAllByIds(final Set<VariantId> ids) {
    if (ids.isEmpty()) {
      return Map.of();
    }

    final var variantById = this.loadAllPort.loadAllByIds(ids);
    final var missing = ids.stream().filter(id -> !variantById.containsKey(id))
        .collect(Collectors.toUnmodifiableSet());
    if (!missing.isEmpty()) {
      throw new VariantsNotFoundException(missing);
    }

    final var soldCountById = this.loadAllSoldCountsPort.loadAllByIds(ids);
    final var stockCountById = this.loadAllStockCountsPort.loadAllByIds(ids);

    final var variantViewCollector = Collectors.toUnmodifiableMap(Variant::getId,
        variant -> toView(variant, soldCountById, stockCountById));
    return variantById.values().stream().collect(variantViewCollector);
  }

  private VariantView toView(final Variant variant,
      final Map<VariantId, VariantSoldCount> soldCountById,
      final Map<VariantId, VariantStockCount> stockCountById) {
    final var variantId = variant.getId();
    final var productId = variant.getProductId();

    final var soldCount =
        soldCountById.getOrDefault(variantId, VariantSoldCount.zero(variantId, productId));
    final var stockCount =
        stockCountById.getOrDefault(variantId, VariantStockCount.zero(variantId, productId));

    return this.mapper.toView(variant, soldCount, stockCount);
  }
}
