package vn.edu.uit.msshop.product.variant.domain.model.creation;

import java.util.List;

import vn.edu.uit.msshop.shared.domain.Domains;

public record NewVariantsForNewProduct(List<NewVariantForNewProduct>values){public NewVariantsForNewProduct{for(final var v:values){Domains.requireNonNull(v,"Variant spec must not be null");}}}
