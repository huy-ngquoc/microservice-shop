package vn.edu.uit.msshop.product.product.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.in.CheckCategoryExistsUseCase;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductCategoryExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;

@Component
@RequiredArgsConstructor
public class CategoryValidationAdapter
        implements CheckProductCategoryExistsPort {
    private final CheckCategoryExistsUseCase checkExistsUseCase;

    @Override
    public boolean existsById(
            final ProductCategoryId categoryId) {
        final var id = new CategoryId(categoryId.value());
        return this.checkExistsUseCase.existsById(id);
    }
}
