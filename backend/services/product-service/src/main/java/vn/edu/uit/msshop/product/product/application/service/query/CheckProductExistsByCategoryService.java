package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsByCategoryUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProductExistsByCategoryPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Service
@RequiredArgsConstructor
public class CheckProductExistsByCategoryService implements CheckProductExistsByCategoryUseCase {
    private final CheckProductExistsByCategoryPort checkExistsByCategoryPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByCategoryId(
            final ProductCategoryId categoryId) {
        return this.checkExistsByCategoryPort.existsByCategoryId(categoryId);
    }

}
