package vn.uit.edu.msshop.cart.domain.model.valueobject;

public record UnitPrice(
        long value) {
    public UnitPrice {
        if (value <= 0)
            throw new IllegalArgumentException("Invalid unit price");
    }

}
