package vn.edu.uit.msshop.product.brand.domain.model;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import vn.edu.uit.msshop.product.brand.domain.model.mutation.BrandDraft;
import vn.edu.uit.msshop.product.brand.domain.model.mutation.BrandUpdateInfo;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

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

    public static Brand create(
            final BrandDraft draft) {
        if (draft == null) {
            throw new DomainException("Draft must NOT be null");
        }

        return new Brand(
                BrandId.newId(),
                draft.name(),
                null);
    }

    public Brand applyUpdateInfo(
            final BrandUpdateInfo updateInfo) {
        if (updateInfo == null) {
            throw new DomainException("Update must NOT be null");
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
