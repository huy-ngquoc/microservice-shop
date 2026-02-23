package vn.edu.uit.msshop.product.domain.model.brand;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.domain.model.brand.command.BrandDraft;
import vn.edu.uit.msshop.product.domain.model.brand.command.BrandUpdate;
import vn.edu.uit.msshop.product.domain.model.brand.snapshot.BrandSnapshot;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public final class Brand {
    @EqualsAndHashCode.Include
    private final BrandId id;

    @NonNull
    private final BrandName name;

    private final BrandLogo logo;

    @NullMarked
    public static Brand create(
            final BrandDraft d) {
        if (d == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        return Brand.builder()
                .name(d.name())
                .logo(d.logo())
                .build();
    }

    @NullMarked
    public static Brand reconstitute(
            final BrandSnapshot s) {
        if (s == null) {
            throw new IllegalArgumentException("Snapshot must NOT be null");
        }

        return Brand.builder()
                .id(s.id())
                .name(s.name())
                .logo(s.logo())
                .build();
    }

    @NullMarked
    public Brand applyUpdate(
            final BrandUpdate u) {
        if (u == null) {
            throw new IllegalArgumentException("Update must NOT be null");
        }

        return Brand.builder()
                .id(this.id)
                .name(u.name())
                .logo(u.logo())
                .build();
    }
}
