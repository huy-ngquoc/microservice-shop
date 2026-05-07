package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateSimpleProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductBrandNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductCategoryNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.CreateProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CreateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.InitializeProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.InitializeProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.InitializeProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.CreateAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.CheckProductBrandExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.CheckProductCategoryExistsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    private final CreateProductPort createPort;
    private final CheckProductCategoryExistsPort checkCategoryExistsPort;
    private final CheckProductBrandExistsPort checkBrandExistsPort;
    private final CreateAllProductVariantsPort createVariantsForNewProductPort;
    private final InitializeProductSoldCountPort initializeSoldCountPort;
    private final InitializeProductStockCountPort initializeStockCountPort;
    private final InitializeProductRatingPort initializeRatingPort;
    private final PublishProductEventPort eventPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.PRODUCT_LIST,
            allEntries = true)
    public ProductView createSimple(
            final CreateSimpleProductCommand command) {
        return CreateProductUseCase.super.createSimple(command);
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.PRODUCT_LIST,
            allEntries = true)
    public ProductView create(
            final CreateProductCommand command) {
        this.validateCategoryExists(command.categoryId());
        this.validateBrandExists(command.brandId());

        final var productId = ProductId.newId();

        final var savedVariants = this.createVariantsForNewProductPort.create(
                productId,
                command.name(),
                command.newConfiguration().newVariants());

        final var configuration = new ProductConfiguration(
                command.newConfiguration().options(),
                savedVariants);

        final var newProduct = new NewProduct(
                productId,
                command.name(),
                command.categoryId(),
                command.brandId(),
                configuration);

        final var savedProduct = this.createPort.create(newProduct);
        final var savedProductId = savedProduct.getId();

        final var savedSoldCount = this.initializeSoldCountPort.initialize(savedProductId);
        final var savedStockCount = this.initializeStockCountPort.initialize(savedProductId);
        final var savedRating = this.initializeRatingPort.initialize(savedProductId);

        this.eventPort.publish(new ProductCreated(savedProductId));
        return this.mapper.toView(
                savedProduct,
                savedSoldCount,
                savedStockCount,
                savedRating);
    }

    private void validateCategoryExists(
            final ProductCategoryId newCategoryId) {
        if (!this.checkCategoryExistsPort.existsById(newCategoryId)) {
            throw new ProductCategoryNotFoundException(newCategoryId);
        }
    }

    private void validateBrandExists(
            final ProductBrandId newBrandId) {
        if (!this.checkBrandExistsPort.existsById(newBrandId)) {
            throw new ProductBrandNotFoundException(newBrandId);
        }
    }

}
