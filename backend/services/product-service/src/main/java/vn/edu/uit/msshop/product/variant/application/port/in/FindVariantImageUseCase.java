package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

public interface FindVariantImageUseCase {
    VariantImageView findImageById(
            final VariantId id);
}
