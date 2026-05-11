package vn.uit.edu.msshop.cart.domain.model.valueobject;

public record ProductName(
        String value) {
    public ProductName {
        if (value == null)
            throw new IllegalArgumentException("Invalid product name");
    }

}
