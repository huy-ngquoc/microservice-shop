package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByVariantIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByVariantIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByVariantIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Service
@RequiredArgsConstructor
class ProductActiveExistenceCheckByVariantIdService
        implements ProductActiveExistenceCheckByVariantIdUseCase {

    private final ProductExistenceCheckByVariantIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean exists(
            final ProductActiveExistenceCheckByVariantIdQuery query) {
        final var variantId = new ProductVariantId(query.variantId());
        return this.existenceCheckPort.existsByVariantId(variantId);
    }
}
