package vn.edu.uit.msshop.product.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.in.CheckCategoryExistsUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.CheckCategoryExistsPort;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

@Service
@RequiredArgsConstructor
public class CheckCategoryExistsService implements CheckCategoryExistsUseCase {
    private final CheckCategoryExistsPort checkExistsPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final CategoryId id) {
        return this.checkExistsPort.existsById(id);
    }
}
