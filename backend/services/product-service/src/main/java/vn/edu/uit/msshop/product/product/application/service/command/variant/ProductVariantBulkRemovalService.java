package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.ArrayList;

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
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkSoftDeletionByIdsPort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Service
@RequiredArgsConstructor
public class ProductVariantBulkRemovalService
        implements ProductVariantBulkRemovalUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductVariantBulkSoftDeletionByIdsPort variantBulkSoftDeletionByIdsPort;
    private final ProductSoldCountLookupByIdPort soldCountLookupByIdPort;
    private final ProductStockCountLookupByIdPort stockCountLookupByIdPort;
    private final ProductRatingLookupByIdPort ratingLookupByIdPort;

    private final ProductViewMapper mapper;
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
    public ProductView removeAll(
            final ProductVariantBulkRemovalCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        final var variantIdList = new ArrayList<ProductVariantId>(cmd.variantIdList().size());
        for (final var variantId : cmd.variantIdList()) {
            final var productVariantId = new ProductVariantId(variantId);
            variantIdList.add(productVariantId);
        }

        // FIXME: O(n^2)?
        var nextVariants = product.getVariants();
        for (final var variantId : variantIdList) {
            nextVariants = nextVariants.removeById(variantId);
        }

        final var newConfig = new ProductConfiguration(
                product.getOptions(),
                nextVariants);
        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfig,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());

        final var savedProduct = this.updatePort.update(next);
        final var savedProductId = savedProduct.getId();

        final var soldCount = this.soldCountLookupByIdPort.loadByIdOrZero(savedProductId);
        final var stockCount = this.stockCountLookupByIdPort.loadByIdOrZero(savedProductId);
        final var rating = this.ratingLookupByIdPort.loadByIdOrZero(savedProductId);

        final var event = new ProductInfoUpdatedEvent(savedProductId);
        this.eventPort.publishEvent(event);

        this.variantBulkSoftDeletionByIdsPort.deleteByIds(variantIdList);

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }
}
