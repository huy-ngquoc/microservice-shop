package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.variant.application.port.out.CheckProductExistsPort;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

@Component
@RequiredArgsConstructor
public class ProductValidationAdapter
        implements CheckProductExistsPort {
    private final CheckProductExistsUseCase checkExistsUseCase;

    @Override
    public boolean existsById(
            final VariantProductId productId) {
        return this.checkExistsUseCase.existsById(new ProductId(productId.value()));
    }

}
