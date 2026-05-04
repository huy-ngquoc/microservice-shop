package vn.edu.uit.msshop.product.product.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        UUID categoryId,
        UUID brandId,
        long minPrice,
        long maxPrice,
        int soldCount,
        int stockCount,
        float ratingAverage,
        int ratingCount,
        List<String> options,
        List<ProductVariantResponse> variants,
        List<String> imageKeys,
        long version) {
    public ProductResponse {
        options = List.copyOf(options);
        variants = List.copyOf(variants);
        imageKeys = List.copyOf(imageKeys);
    }
}
