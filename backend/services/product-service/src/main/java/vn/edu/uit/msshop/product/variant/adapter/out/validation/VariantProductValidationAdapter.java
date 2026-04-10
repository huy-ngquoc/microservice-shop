package vn.edu.uit.msshop.product.variant.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckProductExistsPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantProductValidationAdapter
        implements CheckProductExistsPort {
    private final CheckProductExistsUseCase checkExistsUseCase;

    @Override
    public boolean existsById(
            final VariantProductId productId) {
        return this.checkExistsUseCase.existsById(new ProductId(productId.value()));
    }

}
