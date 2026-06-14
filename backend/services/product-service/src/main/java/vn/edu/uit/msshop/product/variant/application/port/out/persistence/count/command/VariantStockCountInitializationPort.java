package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantStockCount;

public interface VariantStockCountInitializationPort {
    VariantStockCount initialize(
            final NewVariantStockCount newStockCount);
}
