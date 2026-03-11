package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface SaveVariantPort {
    Variant save(
            final Variant variant);
}
