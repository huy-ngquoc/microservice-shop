package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface HardDeleteVariantsForProductUseCase {
  void purgeByProductId(final VariantProductId productId);
}
