package vn.edu.uit.msshop.product.variant.application.port.in.query;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface FindAllVariantsByIdsUseCase {
    Map<VariantId, VariantView> findAllByIds(
            final Set<VariantId> ids);
}
