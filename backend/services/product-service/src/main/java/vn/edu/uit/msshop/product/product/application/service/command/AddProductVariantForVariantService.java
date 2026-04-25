package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductVariantForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;

@Service
@RequiredArgsConstructor
public class AddProductVariantForVariantService
        implements AddProductVariantForVariantUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final PublishProductEventPort eventPort;

    @Override
    @Transactional
    public void addVariant(
            final AddProductVariantForVariantCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var newConfiguration = product.getConfiguration()
                .addVariant(command.variant());

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfiguration,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new ProductUpdated(saved.getId()));
    }
}
