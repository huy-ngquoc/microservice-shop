package vn.edu.uit.msshop.product.product.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.product.application.port.out.validation.CheckProductBrandExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Component
@RequiredArgsConstructor
public class BrandValidationAdapter
        implements
        CheckProductBrandExistsPort {
    private final BrandLookupUseCases.CheckExistsById checkExistsUseCase;

    @Override
    public boolean existsById(
            final ProductBrandId brandId) {
        final var id = new BrandId(brandId.value());
        return this.checkExistsUseCase.existsById(id);
    }
}
