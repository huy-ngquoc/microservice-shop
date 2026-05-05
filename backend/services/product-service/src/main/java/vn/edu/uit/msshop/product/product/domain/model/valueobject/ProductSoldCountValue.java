package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductSoldCountValue(int value){private static final ProductSoldCountValue ZERO=new ProductSoldCountValue(0);

public ProductSoldCountValue{if(value<0){throw new DomainException("Product sold count must NOT be negative");}}

public static ProductSoldCountValue zero(){return ProductSoldCountValue.ZERO;}

public ProductSoldCountValue increase(final int increment){if(increment<0){throw new DomainException("Product sold count increment cannot be negative");}

final var newValue=this.value+increment;return new ProductSoldCountValue(newValue);}

public ProductSoldCountValue decrease(final int decrement){if(decrement<0){throw new DomainException("Product sold count decrement cannot be negative");}

final var newValue=this.value-decrement;if(newValue<0){throw new DomainException("Product sold count cannot decrease below zero: current="+this.value+", decrement="+decrement);}

return new ProductSoldCountValue(newValue);}}
