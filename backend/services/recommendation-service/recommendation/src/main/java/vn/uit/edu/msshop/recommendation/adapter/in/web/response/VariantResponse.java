package vn.uit.edu.msshop.recommendation.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record VariantResponse(
        UUID id,

        UUID productId,

        String productName,

        long price,

        int soldCount,

        int stockCount,

        List<String> traits,

        List<String> targets,

        @Nullable
        String imageKey,

        long version) {
    public VariantResponse {
        traits = List.copyOf(traits);
        targets = List.copyOf(targets);
    }
}
