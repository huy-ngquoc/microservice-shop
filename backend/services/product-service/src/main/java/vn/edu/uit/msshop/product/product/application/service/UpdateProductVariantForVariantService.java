package vn.edu.uit.msshop.product.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.UpdateProductVariantForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;

@Service
@RequiredArgsConstructor
public class UpdateProductVariantForVariantService implements UpdateProductVariantForVariantUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final PublishProductEventPort eventPort;

    @Override
    public void updateVariant(
            final UpdateProductVariantForVariantCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var newVariant = command.updatedVariant();
        final var newVariants = product.getVariants().replaceById(
                newVariant.id(),
                newVariant);
        final var newConfiguration = new ProductConfiguration(
                product.getOptions(),
                newVariants);
        final var newPriceRange = newVariants.getPriceRange();

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newPriceRange,
                product.getSoldCount(),
                product.getRating(),
                newConfiguration,
                product.getImageKeys(),
                product.getVersion());

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new ProductUpdated(saved.getId()));
    }

}
