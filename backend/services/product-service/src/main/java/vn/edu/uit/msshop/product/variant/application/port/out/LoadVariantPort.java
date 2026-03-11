package vn.edu.uit.msshop.product.variant.application.port.out;

import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

public interface LoadVariantPort {
    Optional<Variant> loadById(
            final VariantId id);
}
