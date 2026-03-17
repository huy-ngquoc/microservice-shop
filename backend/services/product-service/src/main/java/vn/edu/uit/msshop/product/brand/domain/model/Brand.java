package vn.edu.uit.msshop.product.brand.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import vn.edu.uit.msshop.product.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class Brand {
    @EqualsAndHashCode.Include
    private final BrandId id;

    private final BrandName name;

    @Nullable
    private final BrandLogoKey logoKey;

    // ===== Metadata =====

    @Nullable
    private final BrandVersion version;

    public Brand(
            final BrandId id,

            final BrandName name,

            @Nullable
            final BrandLogoKey logoKey,

            @Nullable
            final BrandVersion version) {
        this.id = Domains.requireNonNull(id, "Id must NOT be null");
        this.name = Domains.requireNonNull(name, "Name must NOT be null");
        this.logoKey = logoKey;

        this.version = version;
    }
}
