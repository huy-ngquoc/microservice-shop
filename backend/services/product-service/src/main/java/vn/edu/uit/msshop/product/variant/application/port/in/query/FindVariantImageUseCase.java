package vn.edu.uit.msshop.product.variant.application.port.in.query;

import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface FindVariantImageUseCase {
    VariantImageView findImageById(
            final VariantId id);
}
