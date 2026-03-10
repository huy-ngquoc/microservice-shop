package vn.edu.uit.msshop.product.product.application.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.port.in.CreateProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.SaveProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.UploadProductImagePort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductImage;
import vn.edu.uit.msshop.product.product.domain.model.ProductImages;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductPriceRange;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    private final UploadProductImagePort uploadImagePort;
    private final SaveProductPort savePort;
    private final PublishProductEventPort eventPort;

    @Override
    public void create(
            CreateProductCommand command) {
        final var productId = ProductId.newId();

        final var imageList = new ArrayList<ProductImage>(command.images().size());
        for (final var imageCommand : command.images()) {
            final var productImage = this.uploadImagePort.upload(
                    productId,
                    imageCommand.bytes(),
                    imageCommand.originalFilename(),
                    imageCommand.contentType());
            imageList.add(productImage);
        }
        final var productImages = new ProductImages(imageList);

        final var product = new Product(
                productId,
                command.name(),
                productImages,
                ProductPriceRange.zero(),
                ProductSoldCount.zero(),
                ProductRating.zero(),
                command.categoryId(),
                command.brandId(),
                ProductVariants.empty(),
                ProductOptions.empty());

        final var saved = this.savePort.save(product);
        this.eventPort.publish(new ProductCreated(saved.getId()));
    }

}
