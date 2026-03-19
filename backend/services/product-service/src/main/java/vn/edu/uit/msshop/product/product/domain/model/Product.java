package vn.edu.uit.msshop.product.product.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
// TODO: add status
public final class Product {
    @EqualsAndHashCode.Include
    private final ProductId id;

    private final ProductName name;

    private final ProductCategoryId categoryId;

    private final ProductBrandId brandId;

    private final ProductPriceRange priceRange;

    private final ProductSoldCount soldCount;

    private final ProductRating rating;

    private final ProductOptions options;

    private final ProductVariants variants;

    private final ProductImageKeys imageKeys;

    // ===== Metadata =====

    private final ProductVersion version;

    public Product(
            ProductId id,
            ProductName name,
            ProductCategoryId categoryId,
            ProductBrandId brandId,
            ProductPriceRange priceRange,
            ProductSoldCount soldCount,
            ProductRating rating,
            ProductOptions options,
            ProductVariants variants,
            ProductImageKeys imageKeys,
            ProductVersion version) {
        this.id = Domains.requireNonNull(id, "Product ID CANNOT be null");
        this.name = Domains.requireNonNull(name, "Product name CANNOT be null");
        this.categoryId = Domains.requireNonNull(categoryId, "Category ID CANNOT be null");
        this.brandId = Domains.requireNonNull(brandId, "Brand ID CANNOT be null");
        this.priceRange = Domains.requireNonNull(priceRange, "Price range CANNOT be null");
        this.soldCount = Domains.requireNonNull(soldCount, "Sold count CANNOT be null");
        this.rating = Domains.requireNonNull(rating, "Rating CANNOT be null");
        this.options = Domains.requireNonNull(options, "Options list CANNOT be null");
        this.variants = Domains.requireNonNull(variants, "Variants list CANNOT be null");
        this.imageKeys = Domains.requireNonNull(imageKeys, "Product image keys CANNOT be null");
        this.version = Domains.requireNonNull(version, "Product version CANNOT be null");

        this.validateConsistencyBetweenOptionsAndVariants();
    }

    private void validateConsistencyBetweenOptionsAndVariants() {
        if (this.options.isEmpty()) {
            if (this.variants.size() > 1) {
                throw new DomainException(
                        "Cross-Validation failed: Product with NO definition for options CANNOT hold more than 1 variant");
            }

            final var variantsList = this.variants.values();
            if (!variantsList.isEmpty() && !variantsList.getFirst().traits().isEmpty()) {
                throw new DomainException("Default variant must have empty option values");
            }

            return;
        }

        if (this.variants.isEmpty()) {
            throw new DomainException("Product with configurable options MUST have at least one variant");
        }

        final var expectedTierCount = this.options.size();
        for (final var variant : this.variants.values()) {
            if (variant.traits().size() != expectedTierCount) {
                throw new DomainException(
                        String.format(
                                "Cross-Validation failed: Variant '%s' provides %d traits, " +
                                        "but Product defines %d options",
                                variant.id().value(),
                                variant.traits().size(),
                                expectedTierCount));
            }
        }
    }
}
