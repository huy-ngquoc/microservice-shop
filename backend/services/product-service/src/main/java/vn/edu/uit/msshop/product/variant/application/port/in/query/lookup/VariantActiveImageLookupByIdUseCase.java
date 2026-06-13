package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantActiveImageLookupByIdUseCase {
    VariantImageView findImageById(
            final VariantId id);
}
