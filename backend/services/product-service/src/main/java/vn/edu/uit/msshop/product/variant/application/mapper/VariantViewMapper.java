package vn.edu.uit.msshop.product.variant.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.application.dto.query.VariantTraitView;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;

@Component
public class VariantViewMapper {
    public VariantView toView(
            final Variant variant) {
        final var variantTraitViews = variant.getTraits().values()
                .stream().map(this::toView).toList();

        return new VariantView(
                variant.getId().value(),
                variant.getProductId().value(),
                variant.getImage().key().value(),
                variant.getPrice().value(),
                variant.getSold().value(),
                variantTraitViews);
    }

    public VariantTraitView toView(
            final VariantTrait variantTrait) {
        return new VariantTraitView(
                variantTrait.name(),
                variantTrait.value());
    }
}
