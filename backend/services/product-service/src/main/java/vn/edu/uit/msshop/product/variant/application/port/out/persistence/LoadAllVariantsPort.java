package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadAllVariantsPort {
    // TODO: method name is good?
    List<Variant> loadAllByIds(
            final Collection<VariantId> ids);
}
