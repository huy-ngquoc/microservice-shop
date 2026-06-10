package vn.edu.uit.msshop.product.brand.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.BrandProductActiveExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.BrandProductSoftDeletedExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByBrandIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductSoftDeletedExistenceCheckByBrandIdUseCase;

@Component
@RequiredArgsConstructor
public class BrandProductValidationAdapter
        implements
        BrandProductActiveExistenceCheckByBrandIdPort,
        BrandProductSoftDeletedExistenceCheckByBrandIdPort {
    private final ProductActiveExistenceCheckByBrandIdUseCase activeExistenceCheckByBrandIdUseCase;
    private final ProductSoftDeletedExistenceCheckByBrandIdUseCase softDeletedExistenceCheckByBrandIdUseCase;

    @Override
    public boolean existsActiveByBrandId(
            final BrandId brandId) {
        final var query = new ProductActiveExistenceCheckByBrandIdQuery(brandId.value());
        return this.activeExistenceCheckByBrandIdUseCase.exists(query);
    }

    @Override
    public boolean existsSoftDeletedByBrandId(
            final BrandId brandId) {
        final var query = new ProductSoftDeletedExistenceCheckByBrandIdQuery(brandId.value());
        return this.softDeletedExistenceCheckByBrandIdUseCase.exists(query);
    }
}
