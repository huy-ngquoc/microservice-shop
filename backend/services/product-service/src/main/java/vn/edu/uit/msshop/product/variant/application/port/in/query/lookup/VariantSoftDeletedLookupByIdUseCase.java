package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface VariantSoftDeletedLookupByIdUseCase {
    VariantView find(
            final VariantSoftDeletedLookupByIdQuery query);
}
