package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantActiveLookupByIdUseCase {
    VariantView findById(
            final VariantId id);
}
