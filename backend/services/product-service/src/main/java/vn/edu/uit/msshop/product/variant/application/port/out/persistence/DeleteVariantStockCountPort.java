package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface DeleteVariantStockCountPort {
    void deleteById(
            final VariantId id);
}
