package vn.edu.uit.msshop.product.product.application.dto.view;

import java.util.List;
import java.util.UUID;

public record ProductView(
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

        List<ProductVariantView> variants,

        List<String> imageKeys,

        long version) {
}
