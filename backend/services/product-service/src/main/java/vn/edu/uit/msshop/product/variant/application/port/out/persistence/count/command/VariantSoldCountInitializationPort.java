package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;

public interface VariantSoldCountInitializationPort {
    VariantSoldCount initialize(
            final NewVariantSoldCount newSoldCount);
}
