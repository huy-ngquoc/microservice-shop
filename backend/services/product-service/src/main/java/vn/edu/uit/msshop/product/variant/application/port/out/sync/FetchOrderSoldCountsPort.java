package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantOrderSoldCount;

public interface FetchOrderSoldCountsPort {
    Collection<VariantOrderSoldCount> fetchAll();
}
