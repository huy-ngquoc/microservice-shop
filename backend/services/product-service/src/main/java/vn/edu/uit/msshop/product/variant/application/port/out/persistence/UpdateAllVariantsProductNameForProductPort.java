package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;

public interface UpdateAllVariantsProductNameForProductPort {
  void updateProductNameByProductId(final VariantProductId productId,
      final VariantProductName productName);
}
