package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;

public interface UpdateVariantSoldCountPort {
  VariantSoldCount update(final VariantSoldCount soldCount);
}
