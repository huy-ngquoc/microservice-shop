package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface VariantActiveBulkLookupByIdsUseCase {
    Map<UUID, VariantView> findAllByIds(
            final Set<UUID> idSet);
}
