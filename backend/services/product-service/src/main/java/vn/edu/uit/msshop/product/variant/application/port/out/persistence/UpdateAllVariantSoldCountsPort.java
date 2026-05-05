package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;

public interface UpdateAllVariantSoldCountsPort {
  void updateAll(final Collection<VariantSoldCount> soldCounts);
}
