package vn.edu.uit.msshop.product.product.application.service.query.reconciliation;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.reconciliation.ProductReconciliationActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductReconciliationView;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.reconciliation.ProductReconciliationActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class ProductReconciliationActiveLookupByIdService
        implements ProductReconciliationActiveLookupByIdUseCase {

    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public Optional<ProductReconciliationView> find(
            final ProductReconciliationActiveLookupByIdQuery query) {
        final var productId = new ProductId(query.productId());
        return this.activeLookupByIdPort.loadById(productId)
                .map(this.mapper::toReconciliationView);
    }

}
