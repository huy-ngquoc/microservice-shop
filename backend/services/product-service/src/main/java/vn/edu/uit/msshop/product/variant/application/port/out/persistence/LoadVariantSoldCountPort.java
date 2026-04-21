package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadVariantSoldCountPort {
    Optional<VariantSoldCount> loadById(
            final VariantId id);
}
