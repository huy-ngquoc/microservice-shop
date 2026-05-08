package vn.uit.edu.msshop.order.application.port.out;

import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;

public interface CreateVariantSoldCountPort {
    public VariantSoldCount create(VariantSoldCount variantSoldCount);
}
