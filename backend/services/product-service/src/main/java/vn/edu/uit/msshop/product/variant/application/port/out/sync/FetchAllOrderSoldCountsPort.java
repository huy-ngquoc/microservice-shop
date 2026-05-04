package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;

public interface FetchAllOrderSoldCountsPort {
    Collection<VariantOrderSoldCount> fetchAll();
}
