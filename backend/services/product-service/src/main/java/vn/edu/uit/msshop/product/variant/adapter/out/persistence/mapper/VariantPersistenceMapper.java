package vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.adapter.out.persistence.VariantDocument;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSold;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.VariantVersion;

@Component
public class VariantPersistenceMapper {
    public Variant toDomain(
            final VariantDocument entity) {
        final var id = new VariantId(entity.getId());

        final var productId = new VariantProductId(entity.getProductId());
        final var imageKey = VariantImageKey.ofNullable(entity.getImageKey());
        final var price = new VariantPrice(entity.getPrice());
        final var sold = new VariantSold(entity.getSold());
        final var traits = VariantTraits.of(entity.getTraits());

        final var versionValue = Objects.requireNonNull(
                entity.getVersion(),
                "Persisted brand must have a version");
        final var version = new VariantVersion(versionValue);

        return new Variant(
                id,
                productId,
                price,
                sold,
                traits,
                imageKey,
                version);
    }

    public VariantDocument toPersistence(
            final Variant variant) {
        return new VariantDocument(
                variant.getId().value(),
                variant.getProductId().value(),
                variant.getPrice().value(),
                variant.getSold().value(),
                VariantTraits.unwrap(variant.getTraits()),
                VariantImageKey.unwrap(variant.getImageKey()),
                variant.getVersion().value());
    }

    public VariantDocument toPersistence(
            final NewVariant newVariant) {
        return new VariantDocument(
                newVariant.getId().value(),
                newVariant.getProductId().value(),
                newVariant.getPrice().value(),
                VariantSold.zero().value(),
                VariantTraits.unwrap(newVariant.getTraits()),
                null,
                null);
    }
}
