package vn.edu.uit.msshop.product.product.application.exception;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

public final class ProductSimpleCannotAddVariantException
        extends BusinessRuleException {

    public ProductSimpleCannotAddVariantException(
            final ProductId productId) {
        super(String.format(
                "Product '%s' has no options; add an option before adding more variants",
                productId.value()));
    }
}
