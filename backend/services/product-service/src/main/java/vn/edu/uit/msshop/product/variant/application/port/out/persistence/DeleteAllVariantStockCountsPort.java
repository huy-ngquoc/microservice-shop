package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface DeleteAllVariantStockCountsPort {
  void deleteAllByIds(final Collection<VariantId> ids);
}
