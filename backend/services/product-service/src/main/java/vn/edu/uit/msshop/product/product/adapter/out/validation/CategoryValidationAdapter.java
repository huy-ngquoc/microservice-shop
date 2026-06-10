package vn.edu.uit.msshop.product.product.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.query.existence.CategoryActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.category.application.port.in.query.existence.CategoryActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.validation.ProductCategoryExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Component
@RequiredArgsConstructor
public class CategoryValidationAdapter
        implements ProductCategoryExistenceCheckByIdPort {
    private final CategoryActiveExistenceCheckByIdUseCase categoryActiveExistenceCheckByIdUseCase;

    @Override
    public boolean existsById(
            final ProductCategoryId categoryId) {
        final var query = new CategoryActiveExistenceCheckByIdQuery(categoryId.value());
        return this.categoryActiveExistenceCheckByIdUseCase.exists(query);
    }
}
