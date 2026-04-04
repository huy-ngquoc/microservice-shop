package vn.edu.uit.msshop.product.brand.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasProductsPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsByBrandUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Component
@RequiredArgsConstructor
public class BrandProductValidationAdapter
        implements CheckBrandHasProductsPort {
    private final CheckProductExistsByBrandUseCase checkProductExistsByBrandUseCase;

    @Override
    public boolean hasProducts(
            final BrandId brandId) {
        final var productBrandId = new ProductBrandId(brandId.value());
        return this.checkProductExistsByBrandUseCase.existsByBrandId(productBrandId);
    }
}
