package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantDeletionByIdPort {
    void deleteById(
            final VariantId id);
}
