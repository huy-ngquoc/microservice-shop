package vn.edu.uit.msshop.product.variant.application.port.out.validation;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantReferencedByProductCheckPort {
    boolean isReferencedByProduct(
            final VariantId variantId);
}
