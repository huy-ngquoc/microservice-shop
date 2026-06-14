package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantImageActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;

public interface VariantImageActiveLookupByIdUseCase {
    VariantImageView find(
            final VariantImageActiveLookupByIdQuery query);
}
