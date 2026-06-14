package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface VariantUpdatePort {
    Variant update(
            final Variant variant);
}
