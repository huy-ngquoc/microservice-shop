package vn.edu.uit.msshop.product.variant.application.dto.query.listing;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

public record VariantSoftDeletedListingQuery(
        PageRequestDto pageRequest,

        @Nullable
        UUID productId) {
}
