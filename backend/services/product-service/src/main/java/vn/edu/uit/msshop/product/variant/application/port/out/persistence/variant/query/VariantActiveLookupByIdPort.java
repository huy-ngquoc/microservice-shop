package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query;

import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantActiveLookupByIdPort {
    Optional<Variant> loadActiveById(
            final VariantId id);
}
