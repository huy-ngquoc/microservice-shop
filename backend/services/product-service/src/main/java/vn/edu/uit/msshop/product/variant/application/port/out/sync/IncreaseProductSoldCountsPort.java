package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantProductSoldCountIncrement;

public interface IncreaseProductSoldCountsPort {
    void increaseSoldCounts(
            final Collection<VariantProductSoldCountIncrement> increments);
}
