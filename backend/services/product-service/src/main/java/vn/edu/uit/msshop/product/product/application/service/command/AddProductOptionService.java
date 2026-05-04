package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.UpdateAllProductVariantTraitsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class AddProductOptionService implements AddProductOptionUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final UpdateAllProductVariantTraitsPort updateProductVariantTraitsPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductStockCountPort loadStockCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final PublishProductEventPort eventPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    public ProductView addOption(
            AddProductOptionCommand command) {
        final var productId = command.productId();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        if (!expectedVersion.equals(product.getVersion())) {
            throw new OptimisticLockException(
                    expectedVersion.value(), product.getVersion().value());
        }

        final var newConfig = product.getConfiguration().addOption(
                command.newOption(), command.defaultTrait());

        final var newTraitsMap = new HashMap<ProductVariantId, ProductVariantTraits>(
                newConfig.variants().size(), 1);
        for (final var v : newConfig.variants().values()) {
            newTraitsMap.put(v.id(), v.traits());
        }

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

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(savedProductId);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(savedProductId);
        final var rating = this.loadRatingPort.loadByIdOrZero(savedProductId);

        this.eventPort.publish(new ProductUpdated(savedProductId));

        this.updateProductVariantTraitsPort.updateTraitsByIds(newTraitsMap);

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }
}
