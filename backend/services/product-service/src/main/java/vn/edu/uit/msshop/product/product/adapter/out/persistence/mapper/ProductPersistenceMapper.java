package vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.out.persistence.ProductDocument;
import vn.edu.uit.msshop.product.product.adapter.out.persistence.ProductVariantDocument;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductDeletionTime;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKeys;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Component
public class ProductPersistenceMapper {
    public Product toDomain(
            final ProductDocument entity) {
        final var id = new ProductId(entity.getId());

        final var name = new ProductName(entity.getName());
        final var categoryId = new ProductCategoryId(entity.getCategoryId());
        final var brandId = new ProductBrandId(entity.getBrandId());

        final var options = ProductOptions.of(entity.getOptions());

        final var variantsList = entity.getVariants().stream().map(this::toDomain).toList();
        final var variants = new ProductVariants(variantsList);

        final var configuration = new ProductConfiguration(
                options,
                variants);

        final var imageKeys = ProductImageKeys.of(entity.getImageKeys());

        final var versionValue = Objects.requireNonNull(
                entity.getVersion(),
                "Persisted product must have a version");
        final var version = new ProductVersion(versionValue);
        final var deletionTime = ProductDeletionTime.ofNullable(entity.getDeletionTime());

        return new Product(
                id,
                name,
                categoryId,
                brandId,
                configuration,
                imageKeys,
                version,
                deletionTime);
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
        final var priceRange = product.getPriceRange();
        final var variantDocs = product.getVariants().values().stream()
                .map(ProductPersistenceMapper::toPersistence)
                .toList();

        return new ProductDocument(
                product.getId().value(),
                product.getName().value(),
                product.getCategoryId().value(),
                product.getBrandId().value(),
                priceRange.minPrice().value(),
                priceRange.maxPrice().value(),
                product.getOptions().unwrap(),
                variantDocs,
                product.getImageKeys().unwrap(),
                product.getVersion().value(),
                ProductDeletionTime.unwrap(product.getDeletionTime()));
    }

    public ProductDocument toPersistence(
            final NewProduct newProduct) {
        final var priceRange = newProduct.getVariants().getPriceRange();
        final var variantDocs = newProduct.getVariants().values()
                .stream().map(ProductPersistenceMapper::toPersistence).toList();

        return new ProductDocument(
                newProduct.getId().value(),
                newProduct.getName().value(),
                newProduct.getCategoryId().value(),
                newProduct.getBrandId().value(),
                priceRange.minPrice().value(),
                priceRange.maxPrice().value(),
                newProduct.getOptions().unwrap(),
                variantDocs,
                List.of(),
                null,
                null);
    }

    private static ProductVariantDocument toPersistence(
            final ProductVariant variant) {
        return new ProductVariantDocument(
                variant.id().value(),
                variant.price().value(),
                variant.traits().unwrap());
    }
}
