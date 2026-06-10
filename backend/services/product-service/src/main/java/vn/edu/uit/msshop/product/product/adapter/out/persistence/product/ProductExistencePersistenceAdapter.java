package vn.edu.uit.msshop.product.product.adapter.out.persistence.product;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByVariantIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductSoftDeletedExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Component
@RequiredArgsConstructor
class ProductExistencePersistenceAdapter
        implements
        ProductExistenceCheckByIdPort,
        ProductExistenceCheckByBrandIdPort,
        ProductExistenceCheckByCategoryIdPort,
        ProductSoftDeletedExistenceCheckByBrandIdPort,
        ProductSoftDeletedExistenceCheckByCategoryIdPort,
        ProductExistenceCheckByVariantIdPort {
    private final ProductMongoRepository repository;

    @Override
    public boolean existsById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.existsByIdAndDeletionTimeIsNull(jpaId);
    }

    @Override
    public boolean existsByBrandId(
            final ProductBrandId brandId) {
        final var jpaBrandId = brandId.value();
        return this.repository.existsByBrandIdAndDeletionTimeIsNull(jpaBrandId);
    }

    @Override
    public boolean existsByCategoryId(
            final ProductCategoryId categoryId) {
        final var jpaCategoryId = categoryId.value();
        return this.repository.existsByCategoryIdAndDeletionTimeIsNull(jpaCategoryId);
    }

    @Override
    public boolean existsSoftDeletedByBrandId(
            final ProductBrandId brandId) {
        final var jpaBrandId = brandId.value();
        return this.repository.existsByBrandIdAndDeletionTimeIsNotNull(jpaBrandId);
    }

    @Override
    public boolean existsSoftDeletedByCategoryId(
            final ProductCategoryId categoryId) {
        final var jpaCategoryId = categoryId.value();
        return this.repository.existsByCategoryIdAndDeletionTimeIsNotNull(jpaCategoryId);
    }

    @Override
    public boolean existsByVariantId(
            final ProductVariantId variantId) {
        final var jpaVariantId = variantId.value();
        return this.repository.existsByVariants_Id(jpaVariantId);
    }
}
