package vn.edu.uit.msshop.product.product.application.exception;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

public final class ProductMustHaveAtLeastOneVariantException
        extends BusinessRuleException {
    public ProductMustHaveAtLeastOneVariantException(
            final ProductId productId) {
        super("Product '" + productId.value() + "' must have at least one variant");
    }
}
