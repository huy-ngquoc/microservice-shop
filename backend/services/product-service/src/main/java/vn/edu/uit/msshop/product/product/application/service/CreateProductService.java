package vn.edu.uit.msshop.product.product.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateSimpleProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductBrandNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductCategoryNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.CreateProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductBrandExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductCategoryExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.CreateProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.CreateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.model.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTraits;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    private final CreateProductPort createPort;
    private final CheckProductCategoryExistsPort checkCategoryExistsPort;
    private final CheckProductBrandExistsPort checkBrandExistsPort;
    private final CreateProductVariantsPort createVariantsPort;
    private final ProductViewMapper mapper;
    private final PublishProductEventPort eventPort;

    @Override
    @Transactional
    public ProductView create(
            final CreateProductCommand command) {
        return this.create(
                command.categoryId(),
                command.brandId(),
                command.name(),
                command.variants(),
                command.options());
    }

    @Override
    @Transactional
    public ProductView createSimple(
            final CreateSimpleProductCommand command) {
        final var defaultVariant = new CreateProductVariantCommand(
                new ProductVariantPrice(command.price().value()),
                ProductVariantTraits.empty());

        return this.create(
                command.categoryId(),
                command.brandId(),
                command.name(),
                List.of(defaultVariant),
                ProductOptions.empty());
    }

    private ProductView create(
            final ProductCategoryId categoryId,
            final ProductBrandId brandId,
            final ProductName name,
            final List<CreateProductVariantCommand> variants,
            final ProductOptions options) {

        this.validateCategoryExists(categoryId);
        this.validateBrandExists(brandId);

        final var productId = ProductId.newId();

        final var savedVariants = this.createVariantsPort.create(productId, variants);

        final var newProduct = new NewProduct(
                productId,
                name,
                categoryId,
                brandId,
                options,
                savedVariants);

        final var saved = this.createPort.create(newProduct);

        this.eventPort.publish(new ProductCreated(saved.getId()));
        return this.mapper.toView(saved);
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
