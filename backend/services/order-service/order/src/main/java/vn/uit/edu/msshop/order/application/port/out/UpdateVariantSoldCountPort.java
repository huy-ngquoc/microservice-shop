package vn.uit.edu.msshop.order.application.port.out;

import java.util.List;

import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;

public interface UpdateVariantSoldCountPort {
    public VariantSoldCount update(VariantSoldCount variantSoldCount);
    public List<VariantSoldCount> updateMany(List<VariantSoldCount> variantSoldCounts);
}
