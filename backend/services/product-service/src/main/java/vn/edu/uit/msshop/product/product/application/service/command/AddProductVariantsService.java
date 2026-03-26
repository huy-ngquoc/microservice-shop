package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.CreateAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class AddProductVariantsService implements AddProductVariantsUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final CreateAllProductVariantsPort createAllVariantsPort;
    private final PublishProductEventPort eventPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional
    public ProductView addVariants(
            final AddProductVariantsCommand command) {
        final var productId = command.productId();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        if (!expectedVersion.equals(product.getVersion())) {
            throw new OptimisticLockException(
                    expectedVersion.value(), product.getVersion().value());
        }

        for (final var variant : command.newVariants().values()) {
            if (product.getOptions().size() != variant.traits().size()) {
                // TODO: use specified exception.
                throw new RuntimeException("Inconsistent traits size");
            }
        }

        final var createdVariants = this.createAllVariantsPort.create(
                productId,
                command.newVariants());

        final var newVariants = product.getVariants().addAll(createdVariants);
        final var newPriceRange = newVariants.getPriceRange();

        final var newConfiguration = new ProductConfiguration(
                product.getOptions(),
                newVariants);

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

        return this.mapper.toView(saved);
    }
}
