package vn.edu.uit.msshop.product.variant.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class NewVariant {
    @EqualsAndHashCode.Include
    private final VariantId id;

    private final VariantProductId productId;

    private final VariantPrice price;

    private final VariantTraits traits;

    public NewVariant(
            final VariantId id,
            final VariantProductId productId,
            final VariantPrice price,
            final VariantTraits traits) {
        this.id = Domains.requireNonNull(id, "Variant ID must not be null");
        this.productId = Domains.requireNonNull(productId, "Variant Product ID must not be null");
        this.price = Domains.requireNonNull(price, "Variant price must not be null");
        this.traits = Domains.requireNonNull(traits, "Variant traits must not be null");
    }
}
