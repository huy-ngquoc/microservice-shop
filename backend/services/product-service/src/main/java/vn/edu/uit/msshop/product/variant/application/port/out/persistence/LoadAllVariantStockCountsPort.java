package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadAllVariantStockCountsPort {
  Map<VariantId, VariantStockCount> loadAllByIds(final Collection<VariantId> ids);
}
