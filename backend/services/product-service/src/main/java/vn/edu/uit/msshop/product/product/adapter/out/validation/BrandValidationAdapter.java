package vn.edu.uit.msshop.product.product.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.in.CheckBrandExistsUseCase;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductBrandExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;

@Component
@RequiredArgsConstructor
public class BrandValidationAdapter
        implements CheckProductBrandExistsPort {
    private final CheckBrandExistsUseCase checkExistsUseCase;

    @Override
    public boolean existsById(
            final ProductBrandId brandId) {
        final var id = new BrandId(brandId.value());
        return this.checkExistsUseCase.existsById(id);
    }
}
