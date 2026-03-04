package vn.uit.edu.msshop.order.domain.model.valueobject;
public record Discount(long value) {
    public Discount {
        if(value<0) {
            throw new IllegalArgumentException("Invalid discount");
        }
    }
}
