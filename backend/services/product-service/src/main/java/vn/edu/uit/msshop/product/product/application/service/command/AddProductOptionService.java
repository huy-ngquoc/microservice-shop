package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
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
                product.getPriceRange(),
                product.getSoldCount(),
                product.getRating(),
                newConfig,
                product.getImageKeys(),
                product.getVersion());

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new ProductUpdated(saved.getId()));

        this.updateProductVariantTraitsPort.updateTraitsByIds(newTraitsMap);

        return this.mapper.toView(saved);
    }

}
