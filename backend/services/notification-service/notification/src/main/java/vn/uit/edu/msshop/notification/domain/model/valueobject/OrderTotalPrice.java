package vn.uit.edu.msshop.notification.domain.model.valueobject;
public record OrderTotalPrice(int value) {
    public OrderTotalPrice {
        if(value<0) throw new IllegalArgumentException("Invalid order total price");
    }
}
