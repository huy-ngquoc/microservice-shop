package vn.edu.uit.msshop.product.variant.application.port.in.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface RestoreVariantsForProductUseCase {
    void restoreByIds(
            final Collection<VariantId> ids);
}
