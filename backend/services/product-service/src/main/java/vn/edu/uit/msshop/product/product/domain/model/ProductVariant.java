package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.shared.domain.Domains;

public record ProductVariant(ProductVariantId id,ProductVariantPrice price,ProductVariantTraits traits){public ProductVariant{Domains.requireNonNull(id,"Variant ID cannot be null");Domains.requireNonNull(price,"Variant price cannot be null");Domains.requireNonNull(traits,"Variant traits cannot be null");}}
