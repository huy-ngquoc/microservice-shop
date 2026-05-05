package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface RemoveVariantFromProductPort {
  void removeFromProduct(VariantId variantId, VariantProductId variantProductId, int soldDecrement,
      int stockDecrement);
}
