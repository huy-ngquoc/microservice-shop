package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;

public interface VariantSoldCountUpdatePort {
    VariantSoldCount update(
            final VariantSoldCount soldCount);
}
