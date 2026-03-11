package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

public interface FindVariantUseCase {
    VariantView findById(
            final VariantId id);
}
