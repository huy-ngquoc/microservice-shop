package vn.edu.uit.msshop.product.product.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.application.dto.view.ProductReconciliationView;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductVariantView;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Component
public class ProductViewMapper {

    public ProductView toView(
            final Product product,
            final ProductSoldCount soldCount,
            final ProductStockCount stockCount,
            final ProductRating rating) {
        final var priceRange = product.getPriceRange();

        return new ProductView(
                product.getId().value(),
                product.getName().value(),
                product.getCategoryId().value(),
                product.getBrandId().value(),
                priceRange.minPrice().value(),
                priceRange.maxPrice().value(),
                soldCount.getValue().value(),
                stockCount.getValue().value(),
                rating.getTotal().value(),
                rating.getAmount().value(),
                product.getOptions().unwrap(),
                this.toView(product.getVariants()),
                product.getImageKeys().unwrap(),
                product.getVersion().value());
    }

    public ProductReconciliationView toReconciliationView(
            final Product product) {
        final var variantIdSet = product.getVariantIdSet().stream()
                .map(ProductVariantId::value)
                .collect(Collectors.toUnmodifiableSet());
        return new ProductReconciliationView(
                product.getId().value(),
                product.getName().value(),
                variantIdSet);
    }

    public List<ProductVariantView> toView(
            final ProductVariants variants) {
        return variants.getValues().stream()
                .map(this::toView)
                .toList();
    }

    public ProductVariantView toView(
            final ProductVariant variant) {
        return new ProductVariantView(
                variant.id().value(),
                variant.price().value(),
                variant.traits().unwrap());
    }
}
