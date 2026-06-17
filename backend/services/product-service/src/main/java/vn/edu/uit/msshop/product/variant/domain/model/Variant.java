package vn.edu.uit.msshop.product.variant.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;
import vn.edu.uit.msshop.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class Variant {
    @EqualsAndHashCode.Include
    private final VariantId id;

    private final VariantProductId productId;

    private final VariantProductName productName;

    private final VariantPrice price;

    private final VariantTraits traits;

    private final VariantTargets targets;

    @Nullable
    private final VariantImageKey imageKey;

    // ===== Metadata =====

    private final VariantVersion version;

    @Nullable
    private final VariantDeletionTime deletionTime;

    public Variant(
            final VariantId id,

            final VariantProductId productId,

            final VariantProductName productName,

            final VariantPrice price,

            final VariantTraits traits,

            final VariantTargets targets,

            @Nullable
            final VariantImageKey imageKey,

            final VariantVersion version,

            @Nullable
            final VariantDeletionTime deletionTime) {
        this.id = Domains.requireNonNull(id, "Variant ID must not be null");
        this.productId = Domains.requireNonNull(productId, "Variant Product ID must not be null");
        this.productName = Domains.requireNonNull(productName, "Variant Product name must not be null");
        this.price = Domains.requireNonNull(price, "Variant price must not be null");
        this.traits = Domains.requireNonNull(traits, "Variant traits must not be null");
        this.targets = Domains.requireNonNull(targets, "Variant targets must not be null");
        this.imageKey = imageKey;

        this.version = Domains.requireNonNull(version, "Variant version must not be null");
        this.deletionTime = deletionTime;
    }

    public Variant updateInfo(
            final VariantPrice newPrice,
            final VariantTraits newTraits,
            final VariantTargets newTargets) {
        return new Variant(
                this.id,
                this.productId,
                this.productName,
                newPrice,
                newTraits,
                newTargets,
                this.imageKey,
                this.version,
                this.deletionTime);
    }

    public Variant changeTraits(
            final VariantTraits newTraits) {
        return new Variant(
                this.id,
                this.productId,
                this.productName,
                this.price,
                newTraits,
                this.targets,
                this.imageKey,
                this.version,
                this.deletionTime);
    }

    public Variant changeImageKey(
            final VariantImageKey newImageKey) {
        return new Variant(
                this.id,
                this.productId,
                this.productName,
                this.price,
                this.traits,
                this.targets,
                newImageKey,
                this.version,
                this.deletionTime);
    }

    public Variant removeImageKey() {
        return new Variant(
                this.id,
                this.productId,
                this.productName,
                this.price,
                this.traits,
                this.targets,
                null,
                this.version,
                this.deletionTime);
    }

    public Variant softDeleted() {
        return new Variant(
                this.id,
                this.productId,
                this.productName,
                this.price,
                this.traits,
                this.targets,
                this.imageKey,
                this.version,
                VariantDeletionTime.now());
    }

    public Variant restored() {
        return new Variant(
                this.id,
                this.productId,
                this.productName,
                this.price,
                this.traits,
                this.targets,
                this.imageKey,
                this.version,
                null);
    }
}
