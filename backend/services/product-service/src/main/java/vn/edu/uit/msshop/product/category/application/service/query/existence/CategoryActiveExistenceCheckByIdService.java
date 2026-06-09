package vn.edu.uit.msshop.product.category.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.in.query.existence.CategoryActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.existence.CategoryActiveExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
public class CategoryActiveExistenceCheckByIdService
        implements CategoryActiveExistenceCheckByIdUseCase {

    private final CategoryActiveExistenceCheckByIdPort checkExistsPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final CategoryId id) {
        return this.checkExistsPort.existsActiveById(id);
    }
}
