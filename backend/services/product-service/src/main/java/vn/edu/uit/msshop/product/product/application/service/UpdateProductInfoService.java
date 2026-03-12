package vn.edu.uit.msshop.product.product.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductBrandNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductCategoryNotFoundException;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.UpdateProductInfoUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductBrandExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.CheckProductCategoryExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.SaveProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductCreated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
public class UpdateProductInfoService implements UpdateProductInfoUseCase {
    private final LoadProductPort loadPort;
    private final SaveProductPort savePort;
    private final CheckProductCategoryExistsPort checkCategoryExistsPort;
    private final CheckProductBrandExistsPort checkBrandExistsPort;
    private final PublishProductEventPort eventPort;

    @Override
    public void updateInfo(
            final UpdateProductInfoCommand command) {
        final var nameSet = command.name().getSet();
        final var categoryIdSet = command.categoryId().getSet();
        final var brandIdSet = command.brandId().getSet();

        if ((nameSet == null) && (categoryIdSet == null) && (brandIdSet == null)) {
            return;
        }

        final var product = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new ProductNotFoundException(command.id()));

        final var next = this.applyChanges(
                product,
                nameSet,
                categoryIdSet,
                brandIdSet);
        if (next == null) {
            return;
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new ProductCreated(saved.getId()));
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
                current.getImages(),
                current.getPriceRange(),
                current.getSoldCount(),
                current.getRating(),
                newCategoryId,
                newBrandId,
                current.getVariants(),
                current.getOptions());
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
}
