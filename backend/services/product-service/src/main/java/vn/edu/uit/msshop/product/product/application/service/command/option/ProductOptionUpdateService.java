package vn.edu.uit.msshop.product.product.application.service.command.option;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.option.UpdateProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionUpdateUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class ProductOptionUpdateService
        implements
        ProductOptionUpdateUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductSoldCountLookupByIdPort soldCountLookupByIdPort;
    private final ProductStockCountLookupByIdPort stockCountLookupByIdPort;
    private final ProductRatingLookupByIdPort ratingLookupByIdPort;
    private final ProductUpdatePort updatePort;

    private final ProductEventPublicationPort eventPublicationPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            key = "#command.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public ProductView update(
            UpdateProductOptionCommand command) {
        final var productId = command.id();
        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        if (!expectedVersion.equals(product.getVersion())) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    product.getVersion().value());
        }

        final var soldCount = this.soldCountLookupByIdPort.loadByIdOrZero(productId);
        final var stockCount = this.stockCountLookupByIdPort.loadByIdOrZero(productId);
        final var rating = this.ratingLookupByIdPort.loadByIdOrZero(productId);

        final var next = ProductOptionUpdateService.applyChanges(
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
        this.eventPublicationPort.publishEvent(new ProductInfoUpdatedEvent(saved.getId()));

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

        final var configuration = new ProductConfiguration(newOptions, current.getVariants());

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
