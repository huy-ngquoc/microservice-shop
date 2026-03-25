package vn.edu.uit.msshop.product.variant.application.port.out;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

public interface LoadAllVariantsPort {
    List<Variant> loadByIds(
            final Collection<VariantId> ids);
}
