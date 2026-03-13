package vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.adapter.out.persistence.VariantDocument;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSold;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

@Component
public class VariantPersistenceMapper {
    public Variant toDomain(
            final VariantDocument entity) {
        final var id = new VariantId(entity.getId());

        final var productId = new VariantProductId(entity.getProductId());
        final var imageKey = new VariantImageKey(entity.getImageKey());
        final var price = new VariantPrice(entity.getPrice());
        final var sold = new VariantSold(entity.getSold());

        final var traitsList = entity.getTraits()
                .stream().map(VariantTrait::new).toList();
        final var traits = new VariantTraits(traitsList);

        return new Variant(
                id,
                productId,
                imageKey,
                price,
                sold,
                traits);
    }

    public VariantDocument toPersistence(
            final Variant variant) {
        final var traits = variant.getTraits().values()
                .stream().map(VariantTrait::value).toList();

        return new VariantDocument(
                variant.getId().value(),
                variant.getProductId().value(),
                variant.getImageKey().value(),
                variant.getPrice().value(),
                variant.getSold().value(),
                traits);
    }
}
