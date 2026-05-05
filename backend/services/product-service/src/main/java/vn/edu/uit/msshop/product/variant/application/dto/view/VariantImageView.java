package vn.edu.uit.msshop.product.variant.application.dto.view;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record VariantImageView(UUID id,

@Nullable String imageKey,

long version){}
