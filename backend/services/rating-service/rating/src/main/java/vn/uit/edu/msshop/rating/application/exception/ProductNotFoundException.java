package vn.uit.edu.msshop.rating.application.exception;

import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(ProductId id){
        super("Product with id "+id.value()+" does not exists");
    }
}
