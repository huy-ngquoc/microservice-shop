package vn.edu.uit.msshop.product.product.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
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
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    private final CreateProductPort createPort;
    private final CheckProductCategoryExistsPort checkCategoryExistsPort;
    private final CheckProductBrandExistsPort checkBrandExistsPort;
    private final CreateProductVariantsPort createVariantsPort;
    private final PublishProductEventPort eventPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional
    public ProductView create(
            final CreateProductCommand command) {
        this.validateCategoryExists(command.categoryId());
        this.validateBrandExists(command.brandId());

        final var productId = ProductId.newId();

        final var savedVariants = this.createVariantsPort.create(
                productId,
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
