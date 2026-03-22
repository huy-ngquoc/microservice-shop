package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.in.CheckBrandExistsUseCase;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.product.application.port.out.CheckBrandExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;

@Component
@RequiredArgsConstructor
public class BrandValidationAdapter
        implements CheckBrandExistsPort {
    private final CheckBrandExistsUseCase checkExistsUseCase;

    @Override
    public boolean existsById(
            final ProductBrandId id) {
        return this.checkExistsUseCase.existsById(new BrandId(id.value()));
    }
}
