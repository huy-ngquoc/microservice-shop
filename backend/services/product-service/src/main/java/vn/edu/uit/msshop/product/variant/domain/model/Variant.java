package vn.edu.uit.msshop.product.variant.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class Variant {
    @EqualsAndHashCode.Include
    private final VariantId id;

    private final VariantProductId productId;

    private final VariantPrice price;

    // TODO: update each hour... resolve version conflict.
    private final VariantSoldCount soldCount;

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

            final VariantPrice price,

            final VariantSoldCount soldCount,

            final VariantTraits traits,

            final VariantTargets targets,

            @Nullable
            final VariantImageKey imageKey,

            final VariantVersion version,

            @Nullable
            final VariantDeletionTime deletionTime) {
        this.id = Domains.requireNonNull(id, "Variant ID must not be null");
        this.productId = Domains.requireNonNull(productId, "Variant Product ID must not be null");
        this.price = Domains.requireNonNull(price, "Variant price must not be null");
        this.soldCount = Domains.requireNonNull(soldCount, "Variant sold must not be null");
        this.traits = Domains.requireNonNull(traits, "Variant traits must not be null");
        this.targets = Domains.requireNonNull(targets, "Variant targets must not be null");
        this.imageKey = imageKey;

        this.version = Domains.requireNonNull(version, "Variant version must not be null");
        this.deletionTime = deletionTime;
    }
}
