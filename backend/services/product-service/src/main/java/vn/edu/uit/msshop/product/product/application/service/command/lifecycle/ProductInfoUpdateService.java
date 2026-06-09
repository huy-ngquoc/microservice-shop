package vn.edu.uit.msshop.product.product.application.service.command.lifecycle;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductBrandNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductCategoryNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductInfoUpdateUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantNameBulkUpdateForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.ProductBrandExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.ProductCategoryExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.shared.application.dto.Change;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class ProductInfoUpdateService
        implements
        ProductInfoUpdateUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductSoldCountLookupByIdPort soldCountLookupByIdPort;
    private final ProductStockCountLookupByIdPort stockCountLookupByIdPort;
    private final ProductRatingLookupByIdPort ratingLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductCategoryExistenceCheckByIdPort categoryExistenceCheckByIdPort;
    private final ProductBrandExistenceCheckByIdPort brandExistenceCheckByIdPort;

    private final ProductVariantNameBulkUpdateForProductPort variantNameBulkUpdateForProductPort;

    private final ProductEventPublicationPort eventPublicationPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            key = "#command.id().value()",
                            condition = "#command.name().getSet() != null || " +
                                    "#command.categoryId().getSet() != null || " +
                                    "#command.brandId().getSet() != null"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true,
                            condition = "#command.name().getSet() != null || " +
                                    "#command.categoryId().getSet() != null || " +
                                    "#command.brandId().getSet() != null")
            })
    public ProductView updateInfo(
            final UpdateProductInfoCommand command) {
        final var productId = command.id();
        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var soldCount = this.soldCountLookupByIdPort.loadByIdOrZero(productId);
        final var stockCount = this.stockCountLookupByIdPort.loadByIdOrZero(productId);
        final var rating = this.ratingLookupByIdPort.loadByIdOrZero(productId);

        final var nameSet = command.name().getSet();
        final var categoryIdSet = command.categoryId().getSet();
        final var brandIdSet = command.brandId().getSet();

        if ((nameSet == null)
                && (categoryIdSet == null)
                && (brandIdSet == null)) {
            return this.mapper.toView(
                    product,
                    soldCount,
                    stockCount,
                    rating);
        }

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = product.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = this.applyChanges(
                product,
                nameSet,
                categoryIdSet,
                brandIdSet);
        if (next == null) {
            return this.mapper.toView(
                    product,
                    soldCount,
                    stockCount,
                    rating);
        }

        final var savedProduct = this.updatePort.update(next);
        this.syncProductNameToVariantsIfChanged(product, savedProduct);
        this.eventPublicationPort.publishEvent(new ProductInfoUpdatedEvent(savedProduct.getId()));

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }

    private @Nullable Product applyChanges(
            final Product current,
            final Change.@Nullable Set<ProductName> nameSet,
            final Change.@Nullable Set<ProductCategoryId> categoryIdSet,
            final Change.@Nullable Set<ProductBrandId> brandIdSet) {
        final var applyNameResult = Change.Set.applyChange(
                nameSet,
                current.getName());

        final var applyCategoryIdResult = Change.Set.applyChange(
                categoryIdSet,
                current.getCategoryId());
        if (applyCategoryIdResult.changed()) {
            this.validateCategoryExists(applyCategoryIdResult.newValue());
        }

        final var applyBrandIdResult = Change.Set.applyChange(
                brandIdSet,
                current.getBrandId());
        if (applyBrandIdResult.changed()) {
            this.validateBrandExists(applyBrandIdResult.newValue());
        }

        if (!applyNameResult.changed()
                && !applyCategoryIdResult.changed()
                && !applyBrandIdResult.changed()) {
            return null;
        }

        return new Product(
                current.getId(),
                applyNameResult.newValue(),
                applyCategoryIdResult.newValue(),
                applyBrandIdResult.newValue(),
                current.getConfiguration(),
                current.getImageKeys(),
                current.getVersion(),
                current.getDeletionTime());
    }

    private void validateCategoryExists(
            final ProductCategoryId newCategoryId) {
        if (!this.categoryExistenceCheckByIdPort.existsById(newCategoryId)) {
            throw new ProductCategoryNotFoundException(newCategoryId);
        }
    }

    private void validateBrandExists(
            final ProductBrandId newBrandId) {
        if (!this.brandExistenceCheckByIdPort.existsById(newBrandId)) {
            throw new ProductBrandNotFoundException(newBrandId);
        }
    }

    private void syncProductNameToVariantsIfChanged(
            final Product before,
            final Product after) {
        if (after.getName().equals(before.getName())) {
            return;
        }

        this.variantNameBulkUpdateForProductPort.updateProductNameByProductId(
                after.getId(),
                after.getName());
    }
}
