package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadAllVariantsPort {
  Map<VariantId, Variant> loadAllByIds(final Set<VariantId> ids);
}
