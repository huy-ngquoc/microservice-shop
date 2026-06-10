package vn.edu.uit.msshop.product.category.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CategoryProductActiveExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CategoryProductSoftDeletedExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByCategoryIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Component
@RequiredArgsConstructor
public class CategoryProductValidationAdapter
        implements
        CategoryProductActiveExistenceCheckByCategoryIdPort,
        CategoryProductSoftDeletedExistenceCheckByCategoryIdPort {
    private final ProductActiveExistenceCheckByCategoryIdUseCase activeExistenceCheckByBrandIdUseCase;
    private final ProductSoftDeletedExistenceCheckByCategoryIdUseCase softDeletedExistenceCheckByBrandIdUseCase;

    @Override
    public boolean existsActiveByCategoryId(
            final CategoryId id) {
        final var productCategoryId = new ProductCategoryId(id.value());
        return this.activeExistenceCheckByBrandIdUseCase.existsByCategoryId(productCategoryId);
    }

    @Override
    public boolean existsSoftDeletedByCategoryId(
            final CategoryId id) {
        final var productCategoryId = new ProductCategoryId(id.value());
        return this.softDeletedExistenceCheckByBrandIdUseCase
                .existsByCategoryId(productCategoryId);
    }
}
