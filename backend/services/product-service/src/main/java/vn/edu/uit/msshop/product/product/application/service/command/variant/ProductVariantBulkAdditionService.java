package vn.edu.uit.msshop.product.product.application.service.command.variant;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantBulkAdditionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.CreateAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class ProductVariantBulkAdditionService
        implements ProductVariantBulkAdditionUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final CreateAllProductVariantsPort createAllVariantsPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductStockCountPort loadStockCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final PublishProductEventPort eventPort;
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
    public ProductView addAll(
            final AddProductVariantsCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        if (!expectedVersion.equals(product.getVersion())) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    product.getVersion().value());
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
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(savedProductId);
        final var rating = this.loadRatingPort.loadByIdOrZero(savedProductId);

        this.eventPort.publish(new ProductUpdated(savedProductId));

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }
}
