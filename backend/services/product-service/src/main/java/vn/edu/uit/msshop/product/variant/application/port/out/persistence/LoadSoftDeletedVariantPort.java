package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadSoftDeletedVariantPort {
    Optional<Variant> loadSoftDeletedById(
            final VariantId id);
}
