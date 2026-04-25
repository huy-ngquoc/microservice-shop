package vn.edu.uit.msshop.product.product.application.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.application.dto.query.ProductVariantView;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;

@Component
public class ProductViewMapper {
    public ProductView toView(
            final Product product,
            final ProductSoldCount soldCount,
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
                rating.getAverage().value(),
                rating.getAmount().value(),
                product.getOptions().unwrap(),
                this.toView(product.getVariants()),
                product.getImageKeys().unwrap(),
                product.getVersion().value());
    }

    public List<ProductVariantView> toView(
            final ProductVariants variants) {
        return variants.values().stream().map(this::toView).toList();
    }

    public ProductVariantView toView(
            final ProductVariant variant) {
        return new ProductVariantView(
                variant.id().value(),
                variant.price().value(),
                variant.traits().unwrap());
    }
}
