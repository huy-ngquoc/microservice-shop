package vn.edu.uit.msshop.product.brand.domain.model;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class Brand {
    @EqualsAndHashCode.Include
    private final BrandId id;

    private final BrandName name;

    @Nullable
    private final BrandLogo logo;

    public Brand(
            BrandId id,

            BrandName name,

            @Nullable
            BrandLogo logo) {
        this.id = Objects.requireNonNull(id, "Id must NOT be null");
        this.name = Objects.requireNonNull(name, "Name must NOT be null");
        this.logo = logo;
    }
}
