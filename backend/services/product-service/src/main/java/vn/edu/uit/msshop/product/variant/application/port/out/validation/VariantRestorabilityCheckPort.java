package vn.edu.uit.msshop.product.variant.application.port.out.validation;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface VariantRestorabilityCheckPort {
    void validateRestorable(
            final Variant variant);
}
