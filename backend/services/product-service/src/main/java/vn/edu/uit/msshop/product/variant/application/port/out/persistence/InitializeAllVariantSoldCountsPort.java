package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface InitializeAllVariantSoldCountsPort {
    Map<VariantId, VariantSoldCount> initializeAll(
            final Collection<NewVariantSoldCount> newSoldCounts);
}
