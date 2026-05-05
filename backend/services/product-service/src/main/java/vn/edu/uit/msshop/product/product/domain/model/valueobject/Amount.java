package vn.edu.uit.msshop.product.product.domain.model.valueobject;

public record Amount(int value){public Amount{if(value<0)throw new IllegalArgumentException("Invalid amount");}

}
