package vn.uit.edu.msshop.recommendation.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;
import vn.uit.edu.msshop.recommendation.domain.model.Variant;

@Component
public class VariantViewMapper {
    public VariantView toView(Variant variant) {
        return VariantView.builder().variantId(variant.getId().value())
        .name(variant.getName().value())
        .images(variant.getImages().value())
        .targets(variant.getTargets().value())
        .build();
    }
}
