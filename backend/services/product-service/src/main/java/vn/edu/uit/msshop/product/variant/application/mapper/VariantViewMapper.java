package vn.edu.uit.msshop.product.variant.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;

@Component
public class VariantViewMapper {
    public VariantView toView(
            final Variant variant) {
        final var traits = variant.getTraits().values()
                .stream().map(VariantTrait::value).toList();

        return new VariantView(
                variant.getId().value(),
                variant.getProductId().value(),
                variant.getImageKey().value(),
                variant.getPrice().value(),
                variant.getSold().value(),
                traits);
    }
}
