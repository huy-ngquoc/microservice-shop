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
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.CreateAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class AddProductVariantsService implements AddProductVariantsUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final CreateAllProductVariantsPort createAllVariantsPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductRatingPort loadRatingPort;
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
                throw new BusinessRuleException("Inconsistent traits size");
            }
        }

        final var createdVariants = this.createAllVariantsPort.create(
                productId,
                product.getName(),
                command.newVariants());

        final var newVariants = product.getVariants().addAll(createdVariants);
        final var newConfiguration = new ProductConfiguration(
                product.getOptions(),
                newVariants);

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfiguration,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());

        final var savedProduct = this.updatePort.update(next);
        final var savedProductId = savedProduct.getId();

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(savedProductId);
        final var rating = this.loadRatingPort.loadByIdOrZero(savedProductId);

        this.eventPort.publish(new ProductUpdated(savedProductId));

        return this.mapper.toView(
                savedProduct,
                soldCount,
                rating);
    }
}
