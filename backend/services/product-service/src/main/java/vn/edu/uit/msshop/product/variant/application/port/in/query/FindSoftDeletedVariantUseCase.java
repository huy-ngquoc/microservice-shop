package vn.edu.uit.msshop.product.variant.application.port.in.query;

import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface FindSoftDeletedVariantUseCase {
  VariantView findSoftDeletedById(final VariantId id);
}
