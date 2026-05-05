package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantStockCount;

public interface InitializeVariantStockCountPort {
  VariantStockCount initialize(final NewVariantStockCount newStockCount);
}
