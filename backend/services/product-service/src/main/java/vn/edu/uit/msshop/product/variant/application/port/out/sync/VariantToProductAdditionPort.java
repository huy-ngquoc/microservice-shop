package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface VariantToProductAdditionPort {
    void addToProduct(
            final Variant variant,
            int soldIncrement,
            int stockIncrement);
}
