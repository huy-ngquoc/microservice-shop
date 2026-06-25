package vn.edu.uit.msshop.product.variant.application.port.in.query.lookup;

import java.util.List;

import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantProjectionActiveBulkLookupByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantProjectionView;

public interface VariantProjectionActiveBulkLookupByProductIdUseCase {
    List<VariantProjectionView> find(
            final VariantProjectionActiveBulkLookupByProductIdQuery query);
}
