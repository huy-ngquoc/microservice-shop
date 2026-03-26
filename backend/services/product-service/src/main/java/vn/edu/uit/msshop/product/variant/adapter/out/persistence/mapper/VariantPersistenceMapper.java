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
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCount;
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
        final var sold = new VariantSoldCount(entity.getSold());
        final var traits = VariantTraits.of(entity.getTraits());

        final var versionValue = Objects.requireNonNull(
                entity.getVersion(),
                "Persisted brand must have a version");
        final var version = new VariantVersion(versionValue);

        final var deletionTime = VariantDeletionTime.ofNullable(entity.getDeletionTime());

        return new Variant(
                id,
                productId,
                price,
                sold,
                traits,
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
                variant.getSoldCount().value(),
                variant.getTraits().unwrap(),
                VariantImageKey.unwrap(variant.getImageKey()),
                variant.getVersion().value(),
                null);
    }

    public VariantDocument toPersistence(
            final NewVariant newVariant) {
        return new VariantDocument(
                newVariant.getId().value(),
                newVariant.getProductId().value(),
                newVariant.getPrice().value(),
                VariantSoldCount.zero().value(),
                newVariant.getTraits().unwrap(),
                null,
                null,
                null);
    }
}
