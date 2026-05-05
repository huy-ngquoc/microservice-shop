package vn.edu.uit.msshop.product.product.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.UpdateProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class UpdateProductOptionService implements UpdateProductOptionUseCase {
    private final LoadProductPort loadPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductStockCountPort loadStockCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final UpdateProductPort updatePort;
    private final PublishProductEventPort eventPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    public ProductView updateOption(
            UpdateProductOptionCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        if (!expectedVersion.equals(product.getVersion())) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    product.getVersion().value());
        }

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(productId);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(productId);
        final var rating = this.loadRatingPort.loadByIdOrZero(productId);

        final var next = UpdateProductOptionService.applyChanges(
                product,
                command.optionIndex(),
                command.newOption());
        if (next == null) {
            return this.mapper.toView(
                    product,
                    soldCount,
                    stockCount,
                    rating);
        }

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new ProductUpdated(saved.getId()));

        return this.mapper.toView(
                saved,
                soldCount,
                stockCount,
                rating);
    }

    private static @Nullable Product applyChanges(
            final Product current,
            final int optionIndex,
            final ProductOption newOption) {
        final var currentOptions = current.getOptions();

        final var currentOption = currentOptions.getAt(optionIndex);
        if (newOption.equals(currentOption)) {
            return null;
        }

        final var newOptions = currentOptions.replaceAt(optionIndex, newOption);

        final var configuration = new ProductConfiguration(
                newOptions,
                current.getVariants());

        return new Product(
                current.getId(),
                current.getName(),
                current.getCategoryId(),
                current.getBrandId(),
                configuration,
                current.getImageKeys(),
                current.getVersion(),
                current.getDeletionTime());
    }
}
