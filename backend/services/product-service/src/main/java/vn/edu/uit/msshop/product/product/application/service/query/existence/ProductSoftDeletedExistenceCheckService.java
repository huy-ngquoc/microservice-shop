package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.CheckSoftDeletedProductExistsByBrandUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.CheckSoftDeletedProductExistsByCategoryUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.CheckSoftDeletedProductExistsByBrandPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.CheckSoftDeletedProductExistsByCategoryPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSoftDeletedExistenceCheckService
        implements
        CheckSoftDeletedProductExistsByBrandUseCase,
        CheckSoftDeletedProductExistsByCategoryUseCase {
    private final CheckSoftDeletedProductExistsByBrandPort existenceCheckByBrandIdPort;
    private final CheckSoftDeletedProductExistsByCategoryPort existenceCheckByCategoryIdPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsSoftDeletedByBrandId(
            final ProductBrandId brandId) {
        return this.existenceCheckByBrandIdPort.existsSoftDeletedByBrandId(brandId);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsSoftDeletedByCategoryId(
            ProductCategoryId categoryId) {
        return this.existenceCheckByCategoryIdPort.existsSoftDeletedByCategoryId(categoryId);
    }
}
