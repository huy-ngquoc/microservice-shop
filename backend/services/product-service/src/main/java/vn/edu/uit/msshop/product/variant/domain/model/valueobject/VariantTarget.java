package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantTarget(String value){public static final int MAX_LENGTH=30;public static final int MAX_RAW_LENGTH=(int)(MAX_LENGTH*Domains.RAW_LENGTH_TOLERANCE_FACTOR);

public VariantTarget{value=VariantTarget.validateAndNormalizeValue(value);}

private static String validateAndNormalizeValue(final String value){if(value==null){throw new DomainException("Target value CANNOT be null");}

if(value.length()>MAX_RAW_LENGTH){throw new DomainException("Target value wildly exceeds acceptable technical bounds");}

if(value.isBlank()){throw new DomainException("Target value CANNOT be blank");}

final var normalizedValue=Domains.getWhitespacePattern().matcher(value.trim()).replaceAll(" ");

if(normalizedValue.length()>MAX_LENGTH){throw new DomainException("Target value is too long");}

return normalizedValue;}}
