package vn.edu.uit.msshop.product.product.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSimpleCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductBrandNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductCategoryNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductCreationUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountInitializationByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountInitializationByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductCreationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingInitializationByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkCreationPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.ProductBrandExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.ProductCategoryExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;

@Service
@RequiredArgsConstructor
class ProductCreationService
        implements ProductCreationUseCase {
    private final ProductCreationPort creationPort;
    private final ProductCategoryExistenceCheckByIdPort categoryExistenceCheckByIdPort;
    private final ProductBrandExistenceCheckByIdPort brandExistenceCheckByIdPort;
    private final ProductVariantBulkCreationPort variantBulkCreationPort;
    private final ProductSoldCountInitializationByIdPort soldCountInitializationByIdPort;
    private final ProductStockCountInitializationByIdPort stockCountInitializationByIdPort;
    private final ProductRatingInitializationByIdPort ratingInitializationPort;

    private final ProductEventPublicationPort eventPublicationPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.PRODUCT_LIST,
            allEntries = true)
    public ProductView createSimple(
            final ProductSimpleCreationCommand cmd) {
        return ProductCreationUseCase.super.createSimple(cmd);
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.PRODUCT_LIST,
            allEntries = true)
    public ProductView create(
            final ProductCreationCommand cmd) {
        final var categoryId = new ProductCategoryId(cmd.categoryId());
        final var brandId = new ProductBrandId(cmd.brandId());
        final var productName = new ProductName(cmd.productName());

        final var options = ProductOptions.of(cmd.optionList());
        final var newVariants = NewProductVariantData.toNewProductVariants(cmd.variantList());
        final var newConfiguration = new NewProductConfiguration(options, newVariants);

        this.validateCategoryExists(categoryId);
        this.validateBrandExists(brandId);

        final var productId = ProductId.newId();
        final var savedVariants = this.variantBulkCreationPort.create(
                productId,
                productName,
                newConfiguration.newVariants());

        final var configuration = new ProductConfiguration(
                newConfiguration.options(),
                savedVariants);
        final var newProduct = new NewProduct(
                productId,
                productName,
                categoryId,
                brandId,
                configuration);

        final var savedProduct = this.creationPort.create(newProduct);
        final var savedProductId = savedProduct.getId();

        final var savedSoldCount = this.soldCountInitializationByIdPort.initializeById(savedProductId);
        final var savedStockCount = this.stockCountInitializationByIdPort.initializeById(savedProductId);
        final var savedRating = this.ratingInitializationPort.initializeById(savedProductId);

        final var event = new ProductCreatedEvent(savedProductId);
        this.eventPublicationPort.publishEvent(event);

        return this.mapper.toView(
                savedProduct,
                savedSoldCount,
                savedStockCount,
                savedRating);
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
}
