package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductVariantPrice(long value){public ProductVariantPrice{if(value<0){throw new DomainException("Variant price must not be negative");}}}
