package vn.edu.uit.msshop.product.variant.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckProductExistsPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantProductValidationAdapter
        implements CheckProductExistsPort {
    private final ProductActiveExistenceCheckByIdUseCase productActiveExistenceCheckByIdUseCase;

    @Override
    public boolean existsById(
            final VariantProductId productId) {
        final var query = new ProductActiveExistenceCheckByIdQuery(productId.value());
        return this.productActiveExistenceCheckByIdUseCase.exists(query);
    }

}
