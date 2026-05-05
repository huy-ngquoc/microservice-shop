package vn.edu.uit.msshop.product.product.domain.model.creation;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.shared.domain.Domains;

public record NewProductVariant(ProductVariantPrice price,ProductVariantTraits traits,ProductVariantTargets targets){public NewProductVariant{Domains.requireNonNull(price,"Variant price cannot be null");Domains.requireNonNull(traits,"Variant traits cannot be null");Domains.requireNonNull(targets,"Variant targets cannot be null");}

public static NewProductVariant of(final long price,final Collection<String>traits,final Collection<String>targets){return new NewProductVariant(new ProductVariantPrice(price),ProductVariantTraits.of(traits),ProductVariantTargets.of(targets));}}
