package vn.edu.uit.msshop.product.variant.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantSoldCount {
    @EqualsAndHashCode.Include
    private final VariantId id;

    private final VariantProductId productId;

    private final VariantSoldCountValue value;

    public VariantSoldCount(
            final VariantId id,
            final VariantProductId productId,
            final VariantSoldCountValue value) {
        this.id = Domains.requireNonNull(id, "Variant ID must NOT be null");
        this.productId = Domains.requireNonNull(productId, "Variant product ID must NOT be null");
        this.value = Domains.requireNonNull(value, "Variant sold count value must NOT be null");
    }

    public static VariantSoldCount zero(
            final VariantId id,
            final VariantProductId productId) {
        final var value = VariantSoldCountValue.zero();

        return new VariantSoldCount(
                id,
                productId,
                value);
    }
}
