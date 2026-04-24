package vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.adapter.out.persistence.VariantDocument;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

@Component
public class VariantPersistenceMapper {
    public Variant toDomain(
            final VariantDocument entity) {
        final var id = new VariantId(entity.getId());

        final var productId = new VariantProductId(entity.getProductId());
        final var imageKey = VariantImageKey.ofNullable(entity.getImageKey());
        final var price = new VariantPrice(entity.getPrice());
        final var traits = VariantTraits.of(entity.getTraits());
        final var targets = VariantTargets.of(entity.getTargets());

        final var versionValue = Objects.requireNonNull(
                entity.getVersion(),
                "Persisted brand must have a version");
        final var version = new VariantVersion(versionValue);

        final var deletionTime = VariantDeletionTime.ofNullable(entity.getDeletionTime());

        return new Variant(
                id,
                productId,
                price,
                traits,
                targets,
                imageKey,
                version,
                deletionTime);
    }

    public VariantDocument toPersistence(
            final Variant variant) {
        return new VariantDocument(
                variant.getId().value(),
                variant.getProductId().value(),
                variant.getPrice().value(),
                variant.getTraits().unwrap(),
                variant.getTargets().unwrap(),
                VariantImageKey.unwrap(variant.getImageKey()),
                variant.getVersion().value(),
                VariantDeletionTime.unwrap(variant.getDeletionTime()));
    }

    public VariantDocument toPersistence(
            final NewVariant newVariant) {
        return new VariantDocument(
                newVariant.getId().value(),
                newVariant.getProductId().value(),
                newVariant.getPrice().value(),
                newVariant.getTraits().unwrap(),
                newVariant.getTargets().unwrap(),
                null,
                null,
                null);
    }
}
