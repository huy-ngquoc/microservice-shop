package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface VariantActiveLookupByIdUseCase {
    VariantView find(
            final VariantActiveLookupByIdQuery query);
}
