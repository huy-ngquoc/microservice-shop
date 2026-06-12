package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

public interface VariantTraitBulkUpdateByIdsForProductUseCase {
    void updateTraitsByIds(
            final Map<VariantId, VariantTraits> newTraitsMap);
}
