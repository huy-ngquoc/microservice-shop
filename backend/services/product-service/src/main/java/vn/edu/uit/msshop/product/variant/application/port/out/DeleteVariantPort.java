package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

public interface DeleteVariantPort {
    void deleteById(
            final VariantId id);
}
