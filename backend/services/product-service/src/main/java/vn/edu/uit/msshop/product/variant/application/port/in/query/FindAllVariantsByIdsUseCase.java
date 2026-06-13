package vn.edu.uit.msshop.product.variant.application.port.in.query;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface FindAllVariantsByIdsUseCase {
    Map<UUID, VariantView> findAllByIds(
            final Set<UUID> idSet);
}
