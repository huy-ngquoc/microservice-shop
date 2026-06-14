package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import java.util.Collection;
import java.util.Map;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantSoldCountBulkInitializationPort {
    Map<VariantId, VariantSoldCount> initializeAll(
            final Collection<NewVariantSoldCount> newSoldCountCollection);
}
