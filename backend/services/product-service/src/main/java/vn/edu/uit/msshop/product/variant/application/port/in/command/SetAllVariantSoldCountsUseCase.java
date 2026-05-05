package vn.edu.uit.msshop.product.variant.application.port.in.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;

public interface SetAllVariantSoldCountsUseCase {
  void execute(final Collection<VariantOrderSoldCount> orderSoldCounts);
}
