package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface DeleteVariantPort {
    void deleteById(
            final VariantId id);
}
