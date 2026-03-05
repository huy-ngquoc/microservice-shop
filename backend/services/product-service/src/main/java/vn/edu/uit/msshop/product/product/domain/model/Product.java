package vn.edu.uit.msshop.product.product.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class Product {
    @EqualsAndHashCode.Include
    private final ProductId id;

    private final ProductName name;

    private final ProductImages images;

    private final ProductPriceRange priceRange;

    private final ProductSoldCount soldCount;

    private final ProductRating rating;

    private final ProductCategoryId categoryId;

    private final ProductBrandId brandId;

    private final ProductVariants variants;

    private final ProductOptions options;

    public Product(
            ProductId id,
            ProductName name,
            ProductImages images,
            ProductPriceRange priceRange,
            ProductSoldCount soldCount,
            ProductRating rating,
            ProductCategoryId categoryId,
            ProductBrandId brandId,
            ProductVariants variants,
            ProductOptions options) {
        this.id = requireNonNull(id, "Product ID CANNOT be null");
        this.name = requireNonNull(name, "Product name CANNOT be null");
        this.images = requireNonNull(images, "Product images CANNOT be null");
        this.priceRange = requireNonNull(priceRange, "Price range CANNOT be null");
        this.soldCount = requireNonNull(soldCount, "Sold count CANNOT be null");
        this.rating = requireNonNull(rating, "Rating CANNOT be null");
        this.categoryId = requireNonNull(categoryId, "Category ID CANNOT be null");
        this.brandId = requireNonNull(brandId, "Brand ID CANNOT be null");
        this.options = (options != null) ? options : ProductOptions.empty();
        this.variants = (variants != null) ? variants : ProductVariants.empty();

        this.validateConsistencyBetweenOptionsAndVariants();
    }

    private void validateConsistencyBetweenOptionsAndVariants() {
        if (this.options.isEmpty()) {
            if (!this.variants.isEmpty()) {
                throw new DomainException(
                        "Cross-Validation failed: Product with NO definition for options CANNOT hold variants");
            }

            return;
        }

        if (this.variants.isEmpty()) {
            throw new DomainException("Product with configurable options MUST have at least one variant");
        }

        final var expectedTierCount = this.options.getOptionCount();
        for (final var variant : this.variants.values()) {
            if (variant.optionValues().size() != expectedTierCount) {
                throw new DomainException(
                        String.format(
                                "Cross-Validation failed: Variant '%s' provides %d option values, " +
                                        "but Product defines %d option tiers",
                                variant.variantId().value(),
                                variant.optionValues().size(),
                                expectedTierCount));
            }
        }

    }

    private static <T> T requireNonNull(
            @Nullable
            T obj,

            String message) {
        if (obj == null) {
            throw new DomainException(message);
        }

        return obj;
    }
}
