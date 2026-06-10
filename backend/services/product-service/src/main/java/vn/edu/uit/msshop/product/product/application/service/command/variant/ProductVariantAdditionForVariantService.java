package vn.edu.uit.msshop.product.product.application.service.command.variant;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantAdditionForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantAdditionForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;

@Service
@RequiredArgsConstructor
public class ProductVariantAdditionForVariantService
        implements ProductVariantAdditionForVariantUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;

    private final ProductEventPublicationPort eventPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            key = "#cmd.productId()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public void add(
            final ProductVariantAdditionForVariantCommand cmd) {
        final var productId = new ProductId(cmd.productId());

        final var variantId = new ProductVariantId(cmd.variantId());
        final var variantPrice = new ProductVariantPrice(cmd.variantPrice());
        final var variantTraits = ProductVariantTraits.of(cmd.variantTraitList());
        final var newVariant = new ProductVariant(
                variantId,
                variantPrice,
                variantTraits);

        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var currentConfig = product.getConfiguration();
        final var newConfig = currentConfig.addVariant(newVariant);

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfig,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());

        final var saved = this.updatePort.update(next);

        final var event = new ProductInfoUpdatedEvent(saved.getId());
        this.eventPort.publishEvent(event);
    }
}
