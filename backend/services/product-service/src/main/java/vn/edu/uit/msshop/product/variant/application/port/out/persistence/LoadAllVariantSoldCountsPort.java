package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface LoadAllVariantSoldCountsPort {
    List<VariantSoldCount> loadAllByIds(
            final Collection<VariantId> ids);
}
