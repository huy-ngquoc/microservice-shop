package vn.edu.uit.msshop.product.variant.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantSoldCount {
    private final VariantId id;

    private final VariantSoldCountValue value;

    public VariantSoldCount(
            final VariantId id,
            final VariantSoldCountValue value) {
        this.id = Domains.requireNonNull(id, "Variant ID must NOT be null");
        this.value = Domains.requireNonNull(value, "Variant sold count value must NOT be null");
    }
}
