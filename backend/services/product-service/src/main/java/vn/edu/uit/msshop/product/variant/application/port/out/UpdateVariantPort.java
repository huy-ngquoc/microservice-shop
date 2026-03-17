package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface UpdateVariantPort {
    Variant update(
            final Variant variant);
}
