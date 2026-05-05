package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductCategoryId(UUID value)implements Comparable<ProductCategoryId>{public ProductCategoryId{if(value==null){throw new DomainException("Product category ID is null");}}

@Override public int compareTo(final ProductCategoryId other){return this.value.compareTo(other.value);}

}
