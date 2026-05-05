package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTarget;

public record ProductVariantTarget(String value){public static final int MAX_LENGTH=VariantTarget.MAX_LENGTH;public static final int MAX_RAW_LENGTH=VariantTarget.MAX_RAW_LENGTH;

public ProductVariantTarget{value=ProductVariantTarget.validateAndNormalizeValue(value);}

private static String validateAndNormalizeValue(final String value){if(value==null){throw new DomainException("Target value CANNOT be null");}

if(value.length()>MAX_RAW_LENGTH){throw new DomainException("Target value wildly exceeds acceptable technical bounds");}

if(value.isBlank()){throw new DomainException("Target value CANNOT be blank");}

final var normalizedValue=Domains.getWhitespacePattern().matcher(value.trim()).replaceAll(" ");

if(normalizedValue.length()>MAX_LENGTH){throw new DomainException("Target value is too long");}

return normalizedValue;}}
