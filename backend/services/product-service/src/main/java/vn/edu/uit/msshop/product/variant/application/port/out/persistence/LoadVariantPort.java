package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.List;
import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadVariantPort {
    Optional<Variant> loadById(
            final VariantId id);
    public List<Variant> loadByListIds(List<VariantId> ids);
}
