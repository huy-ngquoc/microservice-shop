package vn.edu.uit.msshop.product.product.application.service.command.option;

import java.util.HashMap;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionRemovalByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionRemovalByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantTraitBulkUpdatePort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.event.ProductOptionRemovedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
class ProductOptionRemovalByIdService
        implements ProductOptionRemovalByIdUseCase {
    private final ProductActiveLookupByIdPort activeLookupById;
    private final ProductUpdatePort updatePort;
    private final ProductVariantTraitBulkUpdatePort variantTraitBulkUpdatePort;
    private final ProductSoldCountLookupByProductIdPort soldCountLookupByProductIdPort;
    private final ProductStockCountLookupByProductIdPort stockCountLookupByProductIdPort;
    private final ProductRatingLookupByProductIdPort ratingLookupByProductIdPort;

    private final ProductEventPublicationPort eventPublicationPort;
    private final ProductViewMapper mapper;

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
    public ProductView remove(
            final ProductOptionRemovalByIdCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.activeLookupById.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        ProductOptionRemovalByIdService.ensureOptionRemovable(
                product,
                cmd.optionIndex());

        final var next = product.removeOptionAt(cmd.optionIndex());
        final var savedProduct = this.updatePort.update(next);

        final var savedProductId = savedProduct.getId();
        this.syncVariantTraits(
                next.getVariants(),
                savedProductId);

        final var event = new ProductInfoUpdatedEvent(productId);
        this.eventPublicationPort.publishEvent(event);

        // TODO: omit these
        final var soldCount = this.soldCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var stockCount = this.stockCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var rating = this.ratingLookupByProductIdPort.loadByProductIdOrZero(savedProductId);

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }

    private static void ensureOptionRemovable(
            final Product product,
            final int optionIndex) {
        if (!product.getOptions().isValidIndex(optionIndex)) {
            throw new BusinessRuleException("Option index out of bounds: " + optionIndex);
        }
        if (product.getVariants().collapsesOnTraitRemovalAt(optionIndex)) {
            throw new BusinessRuleException(
                    "Cannot remove this option because it would merge multiple variants into one; "
                            + "remove the duplicate variations first");
        }
    }

    private void syncVariantTraits(
            final ProductVariants variants,
            final ProductId productId) {
        final var newTraitsByVariantId = HashMap.<ProductVariantId, ProductVariantTraits>newHashMap(variants.size());
        for (final var variant : variants.getValues()) {
            final var variantId = variant.id();
            final var variantTraits = variant.traits();

            newTraitsByVariantId.put(variantId, variantTraits);
        }
        this.variantTraitBulkUpdatePort.updateTraitsByIds(
                newTraitsByVariantId,
                productId);

        final var event = new ProductOptionRemovedEvent(
                productId,
                newTraitsByVariantId);
        this.eventPublicationPort.publishEvent(event);
    }
}
