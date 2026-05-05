package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface InitializeAllVariantStockCountsPort {
  Map<VariantId, VariantStockCount> initializeAll(
      final Collection<NewVariantStockCount> newStockCounts);
}
