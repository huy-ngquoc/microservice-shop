package vn.edu.uit.msshop.product.product.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductBrandNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductCategoryNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.UpdateProductInfoUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.UpdateProductNameOnVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.CheckProductBrandExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.validation.CheckProductCategoryExistsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.shared.application.dto.Change;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class UpdateProductInfoService implements UpdateProductInfoUseCase {
    private final LoadProductPort loadPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final UpdateProductPort updatePort;
    private final UpdateProductNameOnVariantsPort updateVariantProductNamePort;
    private final CheckProductCategoryExistsPort checkCategoryExistsPort;
    private final CheckProductBrandExistsPort checkBrandExistsPort;
    private final ProductViewMapper mapper;
    private final PublishProductEventPort eventPort;

    @Override
    @Transactional
    public ProductView updateInfo(
            final UpdateProductInfoCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(productId);
        final var rating = this.loadRatingPort.loadByIdOrZero(productId);

        final var nameSet = command.name().getSet();
        final var categoryIdSet = command.categoryId().getSet();
        final var brandIdSet = command.brandId().getSet();

        if ((nameSet == null) && (categoryIdSet == null) && (brandIdSet == null)) {
            return this.mapper.toView(
                    product,
                    soldCount,
                    rating);
        }

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = product.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = this.applyChanges(
                product,
                nameSet,
                categoryIdSet,
                brandIdSet);
        if (next == null) {
            return this.mapper.toView(
                    product,
                    soldCount,
                    rating);
        }

        final var savedProduct = this.updatePort.update(next);
        this.syncProductNameToVariantsIfChanged(product, savedProduct);
        this.eventPort.publish(new ProductUpdated(savedProduct.getId()));

        return this.mapper.toView(
                savedProduct,
                soldCount,
                rating);
    }

    private @Nullable Product applyChanges(
            final Product current,
            final Change.@Nullable Set<ProductName> nameSet,
            final Change.@Nullable Set<ProductCategoryId> categoryIdSet,
            final Change.@Nullable Set<ProductBrandId> brandIdSet) {
        final ProductName newName;
        final boolean nameUnchanged;
        if ((nameSet != null) && !nameSet.value().equals(current.getName())) {
            newName = nameSet.value();
            nameUnchanged = false;
        } else {
            newName = current.getName();
            nameUnchanged = true;
        }

        final ProductCategoryId newCategoryId;
        final boolean categoryIdUnchanged;
        if ((categoryIdSet != null) && !categoryIdSet.value().equals(current.getCategoryId())) {
            newCategoryId = categoryIdSet.value();
            categoryIdUnchanged = false;

            this.validateCategoryExists(newCategoryId);
        } else {
            newCategoryId = current.getCategoryId();
            categoryIdUnchanged = true;
        }

        final ProductBrandId newBrandId;
        final boolean brandIdUnchanged;
        if ((brandIdSet != null) && !brandIdSet.value().equals(current.getBrandId())) {
            newBrandId = brandIdSet.value();
            brandIdUnchanged = false;

            this.validateBrandExists(newBrandId);
        } else {
            newBrandId = current.getBrandId();
            brandIdUnchanged = true;
        }

        if (nameUnchanged && categoryIdUnchanged && brandIdUnchanged) {
            return null;
        }

        return new Product(
                current.getId(),
                newName,
                newCategoryId,
                newBrandId,
                current.getConfiguration(),
                current.getImageKeys(),
                current.getVersion(),
                current.getDeletionTime());
    }

    private void validateCategoryExists(
            final ProductCategoryId newCategoryId) {
        if (!this.checkCategoryExistsPort.existsById(newCategoryId)) {
            throw new ProductCategoryNotFoundException(newCategoryId);
        }
    }

    private void validateBrandExists(
            final ProductBrandId newBrandId) {
        if (!this.checkBrandExistsPort.existsById(newBrandId)) {
            throw new ProductBrandNotFoundException(newBrandId);
        }
    }

    private void syncProductNameToVariantsIfChanged(
            final Product before,
            final Product after) {
        if (after.getName().equals(before.getName())) {
            return;
        }

        this.updateVariantProductNamePort.updateProductNameByProductId(
                after.getId(),
                after.getName());
    }
}
