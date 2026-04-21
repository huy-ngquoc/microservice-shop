package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;

public interface UpdateAllVariantSoldCountsPort {
    List<VariantSoldCount> updateAll(
            final Collection<VariantSoldCount> soldCounts);
}
