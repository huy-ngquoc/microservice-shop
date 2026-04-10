package vn.edu.uit.msshop.product.variant.application.port.in.command;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface SoftDeleteAllVariantsUseCase {
    void deleteByIds(
            final List<VariantId> ids);
}
