package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

public interface DeleteVariantUseCase {
    void deleteById(
            final VariantId id);
}
