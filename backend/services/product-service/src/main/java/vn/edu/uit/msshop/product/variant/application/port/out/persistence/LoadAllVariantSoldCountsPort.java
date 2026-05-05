package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadAllVariantSoldCountsPort {
  Map<VariantId, VariantSoldCount> loadAllByIds(final Set<VariantId> ids);
}
