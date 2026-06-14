package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;

public interface VariantSoldCountBulkUpdatePort {
    void updateAll(
            final Collection<VariantSoldCount> soldCountCollection);
}
