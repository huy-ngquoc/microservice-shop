package vn.edu.uit.msshop.product.variant.application.service.query.lookup;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantProjectionActiveBulkLookupByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantProjectionView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantProjectionActiveBulkLookupByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
class VariantProjectionActiveBulkLookupByProductIdService
        implements VariantProjectionActiveBulkLookupByProductIdUseCase {

    private final VariantActiveBulkLookupByProductIdPort activeBulkLookupByProductIdPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public List<VariantProjectionView> find(
            final VariantProjectionActiveBulkLookupByProductIdQuery query) {
        final var productId = new VariantProductId(query.productId());
        return this.activeBulkLookupByProductIdPort.loadAllActiveByProductId(productId).stream()
                .map(this.mapper::toProjectionView)
                .toList();
    }

}
