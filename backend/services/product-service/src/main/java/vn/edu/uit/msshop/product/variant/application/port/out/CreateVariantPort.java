package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.model.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface CreateVariantPort {
    Variant create(
            final NewVariant newVariant);
}
