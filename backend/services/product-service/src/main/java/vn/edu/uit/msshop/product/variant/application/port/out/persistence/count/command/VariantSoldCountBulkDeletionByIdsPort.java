package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantSoldCountBulkDeletionByIdsPort {
    void deleteAllByIds(
            final Collection<VariantId> ids);
}
