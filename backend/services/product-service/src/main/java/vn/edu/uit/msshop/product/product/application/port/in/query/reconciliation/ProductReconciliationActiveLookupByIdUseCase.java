package vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation;

import java.util.Optional;

import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductReconciliationView;

public interface ProductReconciliationActiveLookupByIdUseCase {
    Optional<ProductReconciliationView> find(
            final ProductReconciliationActiveLookupByIdQuery query);
}
