package vn.edu.uit.msshop.product.variant.application.port.in.query;

import java.util.List;

import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface FindVariantUseCase {
    VariantView findById(
            final VariantId id);
    public List<VariantView> findByListIds(final List<VariantId> ids);
}
