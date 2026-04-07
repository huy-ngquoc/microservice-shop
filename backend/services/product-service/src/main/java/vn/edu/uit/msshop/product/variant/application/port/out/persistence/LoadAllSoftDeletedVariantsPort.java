package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadAllSoftDeletedVariantsPort {
    List<Variant> loadAllSoftDeletedByIds(
            final Collection<VariantId> ids);
}
