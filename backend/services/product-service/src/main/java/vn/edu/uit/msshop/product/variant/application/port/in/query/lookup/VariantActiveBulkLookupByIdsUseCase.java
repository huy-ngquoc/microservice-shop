package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import java.util.Map;
import java.util.UUID;

import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantActiveBulkLookupByIdsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface VariantActiveBulkLookupByIdsUseCase {
    Map<UUID, VariantView> find(
            final VariantActiveBulkLookupByIdsQuery query);
}
