package vn.edu.uit.msshop.product.variant.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantStockCount {
    @EqualsAndHashCode.Include
    private final VariantId variantId;

    private final VariantProductId productId;

    private final VariantStockCountValue value;

    public VariantStockCount(
            final VariantId variantId,
            final VariantProductId productId,
            final VariantStockCountValue value) {
        this.variantId = Domains.requireNonNull(variantId, "Variant ID must NOT be null");
        this.productId = Domains.requireNonNull(productId, "Variant product ID must NOT be null");
        this.value = Domains.requireNonNull(value, "Variant stock count value must NOT be null");
    }

    public static VariantStockCount zero(
            final VariantId variantId,
            final VariantProductId productId) {
        final var value = VariantStockCountValue.zero();

        return new VariantStockCount(
                variantId,
                productId,
                value);
    }
}
