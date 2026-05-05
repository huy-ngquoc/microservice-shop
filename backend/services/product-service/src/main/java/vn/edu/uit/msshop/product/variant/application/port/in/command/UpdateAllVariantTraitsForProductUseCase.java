package vn.edu.uit.msshop.product.variant.application.port.in.command;

import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

public interface UpdateAllVariantTraitsForProductUseCase {
  void updateTraitsByIds(final Map<VariantId, VariantTraits> newTraitsMap);
}
