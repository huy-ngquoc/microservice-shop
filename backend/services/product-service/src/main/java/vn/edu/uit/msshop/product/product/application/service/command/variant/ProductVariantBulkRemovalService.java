package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantBulkRemovalCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantBulkRemovalUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVariantGuard;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductVariantBulkRemovedEvent;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Service
@RequiredArgsConstructor
class ProductVariantBulkRemovalService
        implements ProductVariantBulkRemovalUseCase {

    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductSoldCountLookupByProductIdPort soldCountLookupByProductIdPort;
    private final ProductStockCountLookupByProductIdPort stockCountLookupByProductIdPort;
    private final ProductRatingLookupByProductIdPort ratingLookupByProductIdPort;

    private final ProductViewMapper mapper;
    private final ProductEventPublicationPort eventPublicationPort;

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
    public ProductView removeAll(
            final ProductVariantBulkRemovalCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        final var variantIdSetToRemove = cmd.variantIdSet().stream()
                .map(ProductVariantId::new)
                .collect(Collectors.toUnmodifiableSet());

        ProductVariantGuard.ensureAllVariantsExist(
                product,
                variantIdSetToRemove);
        ProductVariantGuard.ensureAtLeastOneVariantRemains(
                product,
                variantIdSetToRemove);

        final var next = product.removeVariantsByIds(variantIdSetToRemove);

        final var savedProduct = this.updatePort.update(next);
        final var savedProductId = savedProduct.getId();

        // TODO: omit these
        final var soldCount = this.soldCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var stockCount = this.stockCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var rating = this.ratingLookupByProductIdPort.loadByProductIdOrZero(savedProductId);

        final var event = new ProductVariantBulkRemovedEvent(
                savedProductId,
                variantIdSetToRemove);
        this.eventPublicationPort.publishEvent(event);

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }
}
