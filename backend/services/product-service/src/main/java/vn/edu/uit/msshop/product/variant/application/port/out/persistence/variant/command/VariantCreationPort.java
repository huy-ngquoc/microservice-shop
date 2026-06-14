package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;

public interface VariantCreationPort {
    Variant create(
            final NewVariant newVariant);
}
