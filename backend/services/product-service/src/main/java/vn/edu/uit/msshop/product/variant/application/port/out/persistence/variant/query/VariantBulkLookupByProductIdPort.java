package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

// TODO: split this
public interface VariantBulkLookupByProductIdPort {
    List<Variant> loadAllByProductId(
            final VariantProductId id);

    List<Variant> loadAllActiveByProductId(
            final VariantProductId id);
}
