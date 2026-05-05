package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductPrice(long value){private static final ProductPrice ZERO=new ProductPrice(0);

public ProductPrice{if(value<0){throw new DomainException("Price must not be negative");}}

public static @Nullable ProductPrice ofNullable(@Nullable final Long value){if(value==null){return null;}

return new ProductPrice(value);}

public static ProductPrice zero(){return ProductPrice.ZERO;}}
