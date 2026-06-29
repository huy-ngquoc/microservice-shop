package vn.edu.uit.msshop.product.variant.application.exception;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

public final class VariantLastActiveForProductException
        extends BusinessRuleException {

    public VariantLastActiveForProductException(
            final VariantProductId productId) {
        super("Product '"
                + productId.value()
                + "' must have at least one active variant");
    }
}
