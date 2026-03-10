package vn.edu.uit.msshop.product.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductBrandNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductCategoryNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.UpdateProductInfoUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductBrandExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductCategoryExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.SaveProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;

@Service
@RequiredArgsConstructor
public class UpdateProductInfoService implements UpdateProductInfoUseCase {
    private final LoadProductPort loadPort;
    private final SaveProductPort savePort;
    private final CheckProductCategoryExistsPort checkCategoryExistsPort;
    private final CheckProductBrandExistsPort checkBrandExistsPort;
    private final PublishProductEventPort eventPort;

    public void updateInfo(
            final UpdateProductInfoCommand command) {
        final var product = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new ProductNotFoundException(command.id()));

        final var categoryId = command.categoryId().fold(
                product::getCategoryId,
                this::requireCategoryExists);

        final var brandId = command.brandId().fold(
                product::getBrandId,
                this::requireBrandExists);

        final var next = new Product(
                product.getId(),
                command.name().apply(product.getName()),
                product.getImages(),
                product.getPriceRange(),
                product.getSoldCount(),
                product.getRating(),
                categoryId,
                brandId,
                product.getVariants(),
                product.getOptions());

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new ProductCreated(saved.getId()));
    }

    private ProductCategoryId requireCategoryExists(
            final ProductCategoryId newCategoryId) {
        if (!this.checkCategoryExistsPort.existsById(newCategoryId)) {
            throw new ProductCategoryNotFoundException(newCategoryId);
        }
        return newCategoryId;
    }

    private ProductBrandId requireBrandExists(
            final ProductBrandId newBrandId) {
        if (!this.checkBrandExistsPort.existsById(newBrandId)) {
            throw new ProductBrandNotFoundException(newBrandId);
        }
        return newBrandId;
    }
}
