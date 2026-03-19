package vn.uit.edu.msshop.recommendation.application.dto.query;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
@Builder
public record VariantView(
    UUID variantId,
    List<String> images,
    List<String> targets,
    String name
) {

}
