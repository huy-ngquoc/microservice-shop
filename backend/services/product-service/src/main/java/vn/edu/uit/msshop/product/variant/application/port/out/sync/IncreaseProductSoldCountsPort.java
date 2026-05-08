package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface IncreaseProductSoldCountsPort {
    void increaseAllSoldCounts(
            final Map<VariantProductId, Integer> incrementByProductId);
}
