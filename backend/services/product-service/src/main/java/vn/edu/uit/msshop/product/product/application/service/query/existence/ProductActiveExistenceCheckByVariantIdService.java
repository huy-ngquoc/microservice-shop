package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByVariantIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByVariantIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Service
@RequiredArgsConstructor
public class ProductActiveExistenceCheckByVariantIdService
        implements ProductActiveExistenceCheckByVariantIdUseCase {

    private final ProductExistenceCheckByVariantIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByVariantId(
            final ProductVariantId variantId) {
        return this.existenceCheckPort.existsByVariantId(variantId);
    }
}
