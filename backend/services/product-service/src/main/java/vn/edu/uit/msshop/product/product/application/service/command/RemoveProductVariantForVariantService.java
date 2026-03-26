package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.RemoveProductVariantForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;

@Service
@RequiredArgsConstructor
public class RemoveProductVariantForVariantService implements RemoveProductVariantForVariantUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final PublishProductEventPort eventPort;

    @Override
    @Transactional
    public void removeVariant(
            RemoveProductVariantForVariantCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var newVariants = product.getVariants().removeById(command.variantId());
        final var newConfiguration = new ProductConfiguration(
                product.getOptions(),
                newVariants);

        // TODO: should we update sold count and stuffs as well?
        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                product.getPriceRange(),
                product.getSoldCount(),
                product.getRating(),
                newConfiguration,
                product.getImageKeys(),
                product.getVersion());

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new ProductCreated(saved.getId()));
    }
}
