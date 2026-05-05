package vn.edu.uit.msshop.product.category.application.dto.view;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record CategoryView(UUID id,

String name,

@Nullable String imageKey,

long version){}
