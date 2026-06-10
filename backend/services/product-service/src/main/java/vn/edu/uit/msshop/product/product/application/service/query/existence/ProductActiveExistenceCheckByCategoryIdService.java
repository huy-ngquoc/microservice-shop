package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByCategoryIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByCategoryIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Service
@RequiredArgsConstructor
class ProductActiveExistenceCheckByCategoryIdService
        implements ProductActiveExistenceCheckByCategoryIdUseCase {

    private final ProductExistenceCheckByCategoryIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean exists(
            final ProductActiveExistenceCheckByCategoryIdQuery query) {
        final var categoryId = new ProductCategoryId(query.categoryId());
        return this.existenceCheckPort.existsByCategoryId(categoryId);
    }
}
