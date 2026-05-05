package vn.edu.uit.msshop.product.variant.domain.model.creation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class NewVariant {
    @EqualsAndHashCode.Include
    private final VariantId id;

    private final VariantProductId productId;

    private final VariantProductName productName;

    private final VariantPrice price;

    private final VariantTraits traits;

    private final VariantTargets targets;

    public NewVariant(
            final VariantId id,
            final VariantProductId productId,
            final VariantProductName productName,
            final VariantPrice price,
            final VariantTraits traits,
            final VariantTargets targets) {
        this.id = Domains.requireNonNull(id, "Variant ID must not be null");
        this.productId = Domains.requireNonNull(productId, "Variant Product ID must not be null");
        this.productName = Domains.requireNonNull(productName, "Variant Product name must not be null");
        this.price = Domains.requireNonNull(price, "Variant price must not be null");
        this.traits = Domains.requireNonNull(traits, "Variant traits must not be null");
        this.targets = Domains.requireNonNull(targets, "Variant targets must not be null");
    }
}
