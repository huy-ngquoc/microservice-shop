package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface AddVariantToProductPort {
    void addToProduct(
            final Variant variant,
            int soldIncrement,
            int stockIncrement);
}
