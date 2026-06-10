package vn.edu.uit.msshop.product.variant.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByVariantIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByVariantIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckVariantReferencedByProductPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantReferencedByProductValidationAdapter
        implements CheckVariantReferencedByProductPort {
    private final ProductActiveExistenceCheckByVariantIdUseCase productActiveExistenceCheckByVariantIdUseCase;

    @Override
    public boolean isReferencedByProduct(
            final VariantId variantId) {
        final var query = new ProductActiveExistenceCheckByVariantIdQuery(variantId.value());
        return this.productActiveExistenceCheckByVariantIdUseCase.exists(query);
    }
}
