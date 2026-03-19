package vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.out.persistence.ProductDocument;
import vn.edu.uit.msshop.product.product.adapter.out.persistence.ProductVariantDocument;
import vn.edu.uit.msshop.product.product.domain.model.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductImageKeys;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductPriceRange;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.ProductVersion;

@Component
public class ProductPersistenceMapper {
    public Product toDomain(
            final ProductDocument entity) {
        final var id = new ProductId(entity.getId());

        final var name = new ProductName(entity.getName());
        final var categoryId = new ProductCategoryId(entity.getCategoryId());
        final var brandId = new ProductBrandId(entity.getBrandId());
        final var priceRange = new ProductPriceRange(
                new ProductPrice(entity.getMinPrice()),
                new ProductPrice(entity.getMaxPrice()));
        final var soldCount = new ProductSoldCount(entity.getSoldCount());
        final var rating = new ProductRating(
                entity.getRatingAverage(),
                entity.getRatingCount());
        final var options = ProductOptions.of(entity.getOptions());
        final var imageKeys = ProductImageKeys.of(entity.getImageKeys());

        final var variantsList = entity.getVariants().stream().map(this::toDomain).toList();
        final var variants = new ProductVariants(variantsList);

        final var versionValue = Objects.requireNonNull(
                entity.getVersion(),
                "Persisted product must have a version");
        final var version = new ProductVersion(versionValue);

        return new Product(
                id,
                name,
                categoryId,
                brandId,
                priceRange,
                soldCount,
                rating,
                options,
                variants,
                imageKeys,
                version);
    }

    public ProductVariant toDomain(
            final ProductVariantDocument entity) {
        final var id = new ProductVariantId(entity.getId());
        final var price = new ProductVariantPrice(entity.getPrice());
        final var traits = ProductVariantTraits.of(entity.getTraits());

        return new ProductVariant(
                id,
                price,
                traits);
    }

    public ProductDocument toPersistence(
            final Product product) {
        return new ProductDocument(
                product.getId().value(),
                product.getName().value(),
                product.getCategoryId().value(),
                product.getBrandId().value(),
                product.getPriceRange().minPrice().value(),
                product.getPriceRange().maxPrice().value(),
                product.getSoldCount().value(),
                product.getRating().average(),
                product.getRating().count(),
                product.getOptions().unwrap(),
                product.getVariants().values().stream().map(this::toPersistence).toList(),
                product.getImageKeys().unwrap(),
                product.getVersion().value());
    }

    public ProductDocument toPersistence(
            final NewProduct newProduct) {
        return new ProductDocument(
                newProduct.getId().value(),
                newProduct.getName().value(),
                newProduct.getCategoryId().value(),
                newProduct.getBrandId().value(),
                0,
                0,
                0,
                0,
                0,
                List.of(),
                List.of(),
                List.of(),
                null);
    }

    private ProductVariantDocument toPersistence(
            final ProductVariant variant) {
        return new ProductVariantDocument(
                variant.id().value(),
                variant.price().value(),
                variant.traits().unwrap());
    }
}
