package vn.edu.uit.msshop.product.variant.application.port.in;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

public interface SoftDeleteAllVariantsUseCase {
    void deleteByIds(
            final List<VariantId> ids);
}
