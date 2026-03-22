package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.in.CheckCategoryExistsUseCase;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.product.application.port.out.CheckCategoryExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;

@Component
@RequiredArgsConstructor
public class CategoryValidationAdapter
        implements CheckCategoryExistsPort {
    private final CheckCategoryExistsUseCase checkExistsUseCase;

    @Override
    public boolean existsById(
            final ProductCategoryId id) {
        return this.checkExistsUseCase.existsById(new CategoryId(id.value()));
    }
}
