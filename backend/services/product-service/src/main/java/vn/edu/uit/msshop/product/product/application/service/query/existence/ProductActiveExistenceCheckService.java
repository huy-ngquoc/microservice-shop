package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByBrandIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByCategoryIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByVariantIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByVariantIdPort;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Service
@RequiredArgsConstructor
public class ProductActiveExistenceCheckService
        implements
        ProductActiveExistenceCheckByIdUseCase,
        ProductActiveExistenceCheckByBrandIdUseCase,
        ProductActiveExistenceCheckByCategoryIdUseCase,
        ProductActiveExistenceCheckByVariantIdUseCase {
    private final ProductExistenceCheckByIdPort existenceCheckByIdPort;
    private final ProductExistenceCheckByBrandIdPort existenceCheckByBrandIdPort;
    private final ProductExistenceCheckByCategoryIdPort existenceCheckByCategoryIdPort;
    private final ProductExistenceCheckByVariantIdPort existenceCheckByVariantIdPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final ProductId id) {
        return this.existenceCheckByIdPort.existsById(id);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByBrandId(
            final ProductBrandId brandId) {
        return this.existenceCheckByBrandIdPort.existsByBrandId(brandId);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByCategoryId(
            final ProductCategoryId categoryId) {
        return this.existenceCheckByCategoryIdPort.existsByCategoryId(categoryId);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByVariantId(
            final ProductVariantId variantId) {
        return this.existenceCheckByVariantIdPort.existsByVariantId(variantId);
    }
}
