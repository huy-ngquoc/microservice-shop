package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

// TODO: name is too long?
public interface DeleteAllVariantsByProductIdPort {
    void deleteByProductId(
            final VariantProductId id);
}
