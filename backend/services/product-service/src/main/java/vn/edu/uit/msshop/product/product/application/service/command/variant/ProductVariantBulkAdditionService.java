package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.HashSet;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantBulkAdditionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantBulkAdditionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkCreationPort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVariantGuard;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
class ProductVariantBulkAdditionService
        implements ProductVariantBulkAdditionUseCase {

    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductVariantBulkCreationPort variantBulkCreationPort;
    private final ProductSoldCountLookupByProductIdPort soldCountLookupByProductIdPort;
    private final ProductStockCountLookupByProductIdPort stockCountLookupByProductIdPort;
    private final ProductRatingLookupByProductIdPort ratingLookupByProductIdPort;

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
    public ProductView addAll(
            final ProductVariantBulkAdditionCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        ProductVariantBulkAdditionService.ensureVariantsAddable(
                product,
                cmd.newVariantList());

        final var newVariants = NewProductVariantData.toNewProductVariants(cmd.newVariantList());
        final var createdVariants = this.variantBulkCreationPort.create(
                productId,
                product.getName(),
                newVariants);

        final var next = product.addVariants(createdVariants);

        final var savedProduct = this.updatePort.update(next);
        final var savedProductId = savedProduct.getId();

        final var soldCount = this.soldCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var stockCount = this.stockCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var rating = this.ratingLookupByProductIdPort.loadByProductIdOrZero(savedProductId);

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }

    private static void ensureVariantsAddable(
            final Product product,
            final List<NewProductVariantData> newVariantList) {
        ProductVariantGuard.ensureNotProductSimple(product);
        ProductVariantBulkAdditionService.ensureWithinMaxAmount(
                product,
                newVariantList);

        final var expectedTraitCount = product.getOptions().size();
        final var seenCombinations = new HashSet<List<String>>();
        for (final var newVariant : newVariantList) {
            if (expectedTraitCount != newVariant.traitList().size()) {
                throw new BusinessRuleException("Inconsistent traits size");
            }
            final var traits = ProductVariantTraits.of(newVariant.traitList());
            ProductVariantGuard.ensureNoDuplicateCombination(product, traits);
            if (!seenCombinations.add(traits.unwrapNormalized())) {
                throw new BusinessRuleException(
                        "Duplicate trait combination within the request: " + traits.unwrap());
            }
        }
    }

    private static void ensureWithinMaxAmount(
            final Product product,
            final List<NewProductVariantData> newVariantList) {
        final var resultingTotal = product.getVariants().size() + newVariantList.size();
        if (resultingTotal > ProductVariants.MAX_AMOUNT) {
            throw new BusinessRuleException(
                    "Adding " + newVariantList.size()
                            + " variants would exceed the maximum of " + ProductVariants.MAX_AMOUNT);
        }
    }
}
