package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductOption(String value){public static final int MAX_LENGTH_VALUE=20;public static final int MAX_RAW_LENGTH_VALUE=30;

public ProductOption{if(value==null){throw new DomainException("Option value CANNOT be null");}

if(value.length()>MAX_RAW_LENGTH_VALUE){throw new DomainException("Option value wildly exceeds acceptable technical bounds");}

if(value.isBlank()){throw new DomainException("Option value CANNOT be blank");}

value=Domains.getWhitespacePattern().matcher(value.trim()).replaceAll(" ");

if(value.length()>MAX_LENGTH_VALUE){throw new DomainException("Option value is too long");}}}
