package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface DecreaseProductSoldCountsPort {
    void decreaseAllSoldCounts(
            final Map<VariantProductId, Integer> decrementByProductId);
}
