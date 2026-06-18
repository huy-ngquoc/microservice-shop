package vn.edu.uit.msshop.product.variant.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductExistenceCheckByVariantIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductExistenceCheckByVariantIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.VariantReferencedByProductCheckPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantReferencedByProductValidationAdapter
        implements VariantReferencedByProductCheckPort {
    private final ProductExistenceCheckByVariantIdUseCase productExistenceCheckByVariantIdUseCase;

    @Override
    public boolean isReferencedByProduct(
            final VariantId variantId) {
        final var query = new ProductExistenceCheckByVariantIdQuery(variantId.value());
        return this.productExistenceCheckByVariantIdUseCase.exists(query);
    }
}
