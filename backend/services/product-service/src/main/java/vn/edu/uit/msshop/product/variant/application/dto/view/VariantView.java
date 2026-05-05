package vn.edu.uit.msshop.product.variant.application.dto.view;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record VariantView(UUID id,

UUID productId,

String productName,

long price,

int soldCount,

int stockCount,

List<String>traits,

List<String>targets,

@Nullable String imageKey,

long version){public VariantView{traits=List.copyOf(traits);targets=List.copyOf(targets);}}
