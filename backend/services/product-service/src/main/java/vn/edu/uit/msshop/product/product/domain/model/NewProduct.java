package vn.edu.uit.msshop.product.product.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class NewProduct {
    @EqualsAndHashCode.Include
    private final ProductId id;

    private final ProductName name;

    private final ProductCategoryId categoryId;

    private final ProductBrandId brandId;

    private final ProductOptions options;

    private final ProductVariants variants;

    public NewProduct(
            final ProductId id,
            final ProductName name,
            final ProductCategoryId categoryId,
            final ProductBrandId brandId,
            final ProductOptions options,
            final ProductVariants variants) {
        this.id = Domains.requireNonNull(id, "Product ID CANNOT be null");
        this.name = Domains.requireNonNull(name, "Product name CANNOT be null");
        this.categoryId = Domains.requireNonNull(categoryId, "Category ID CANNOT be null");
        this.brandId = Domains.requireNonNull(brandId, "Brand ID CANNOT be null");
        this.options = Domains.requireNonNull(options, "Options CANNOT be null");
        this.variants = Domains.requireNonNull(variants, "Variants CANNOT be null");

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

        final var expectedOptionsCount = this.options.size();
        for (final var variant : this.variants.values()) {
            if (variant.traits().size() != expectedOptionsCount) {
                throw new DomainException(
                        String.format(
                                "Cross-Validation failed: Variant '%s' provides %d traits, " +
                                        "but Product defines %d options",
                                variant.id().value(),
                                variant.traits().size(),
                                expectedOptionsCount));
            }
        }
    }
}
