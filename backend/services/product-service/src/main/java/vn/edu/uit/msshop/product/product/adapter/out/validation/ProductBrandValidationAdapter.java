package vn.edu.uit.msshop.product.product.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.existence.BrandActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.brand.application.port.in.query.existence.BrandActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.validation.ProductBrandExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Component
@RequiredArgsConstructor
public class ProductBrandValidationAdapter
        implements ProductBrandExistenceCheckByIdPort {

    private final BrandActiveExistenceCheckByIdUseCase brandActiveExistenceCheckByIdUseCase;

    @Override
    public boolean existsById(
            final ProductBrandId brandId) {
        final var query = new BrandActiveExistenceCheckByIdQuery(brandId.value());
        return this.brandActiveExistenceCheckByIdUseCase.exists(query);
    }
}
