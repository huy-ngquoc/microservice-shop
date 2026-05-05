package vn.edu.uit.msshop.product.variant.domain.model.creation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class NewVariantSoldCount {
    @EqualsAndHashCode.Include
    private final VariantId variantId;

    private final VariantProductId productId;

    public NewVariantSoldCount(
            final VariantId variantId,
            final VariantProductId productId) {
        this.variantId = Domains.requireNonNull(variantId, "Variant ID must NOT be null");
        this.productId = Domains.requireNonNull(productId, "Variant product ID must NOT be null");
    }
}
