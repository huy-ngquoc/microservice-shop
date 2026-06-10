package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSoftDeletedExistenceCheckByCategoryIdService
        implements ProductSoftDeletedExistenceCheckByCategoryIdUseCase {

    private final ProductSoftDeletedExistenceCheckByCategoryIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean exists(
            final ProductSoftDeletedExistenceCheckByCategoryIdQuery query) {
        final var categoryId = new ProductCategoryId(query.categoryId());
        return this.existenceCheckPort.existsSoftDeletedByCategoryId(categoryId);
    }
}
