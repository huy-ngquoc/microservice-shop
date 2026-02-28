package vn.edu.uit.msshop.product.domain.model.brand;

import java.util.Objects;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.domain.model.brand.command.BrandDraft;
import vn.edu.uit.msshop.product.domain.model.brand.command.BrandUpdateInfo;
import vn.edu.uit.msshop.product.domain.model.brand.snapshot.BrandSnapshot;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class Brand {
    @EqualsAndHashCode.Include
    private final BrandId id;

    private final BrandName name;

    @Nullable
    private final BrandLogo logo;

    private Brand(
            BrandId id,

            BrandName name,

            @Nullable
            BrandLogo logo) {
        this.id = Objects.requireNonNull(id, "Id must NOT be null");
        this.name = Objects.requireNonNull(name, "Name must NOT be null");
        this.logo = logo;
    }

    public static Brand create(
            final BrandDraft draft) {
        if (draft == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        return new Brand(
                BrandId.newId(),
                draft.name(),
                null);
    }

    public static Brand reconstitute(
            final BrandSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException("Snapshot must NOT be null");
        }

        return new Brand(
                snapshot.id(),
                snapshot.name(),
                snapshot.logo());
    }

    public Brand applyUpdateInfo(
            final BrandUpdateInfo updateInfo) {
        if (updateInfo == null) {
            throw new IllegalArgumentException("Update must NOT be null");
        }

        if (this.isSameInfoAs(updateInfo)) {
            return this;
        }

        return new Brand(
                this.id,
                updateInfo.name(),
                this.logo);
    }

    public Brand withLogo(
            final BrandLogo newLogo) {
        if (Objects.equals(newLogo, this.logo)) {
            return this;
        }

        return new Brand(
                this.id,
                this.name,
                newLogo);
    }

    public Brand withoutLogo() {
        if (this.logo == null) {
            return this;
        }

        return new Brand(
                this.id,
                this.name,
                null);
    }

    private boolean isSameInfoAs(
            final BrandUpdateInfo updateInfo) {
        return Objects.equals(updateInfo.name(), this.name);
    }
}
