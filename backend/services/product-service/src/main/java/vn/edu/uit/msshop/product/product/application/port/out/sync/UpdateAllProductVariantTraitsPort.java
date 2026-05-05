package vn.edu.uit.msshop.product.product.application.port.out.sync;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;

public interface UpdateAllProductVariantTraitsPort {
  void updateTraitsByIds(final Map<ProductVariantId, ProductVariantTraits> newTraitsMap);
}
