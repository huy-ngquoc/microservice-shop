package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.RemoveProductVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.SoftDeleteAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class RemoveProductVariantsService implements RemoveProductVariantsUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final SoftDeleteAllProductVariantsPort softDeleteAllVariantsPort;
    private final PublishProductEventPort eventPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional
    public ProductView removeVariants(
            final RemoveProductVariantsCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        if (!expectedVersion.equals(product.getVersion())) {
            throw new OptimisticLockException(
                    expectedVersion.value(), product.getVersion().value());
        }

        var productVariants = product.getVariants();
        final var variantIds = command.variantIds();

        for (final var variantId : variantIds) {
            productVariants = productVariants.removeById(variantId);
        }

        final var newPriceRange = productVariants.getPriceRange();
        final var newConfiguration = new ProductConfiguration(
                product.getOptions(),
                productVariants);
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
                product.getVersion(),
                product.getDeletionTime());

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new ProductUpdated(saved.getId()));

        this.softDeleteAllVariantsPort.deleteByIds(variantIds);

        return this.mapper.toView(saved);
    }
}
