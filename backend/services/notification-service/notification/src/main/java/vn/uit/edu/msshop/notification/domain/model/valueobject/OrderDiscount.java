package vn.uit.edu.msshop.notification.domain.model.valueobject;
public record OrderDiscount(int value) {
    public OrderDiscount {
        if(value<0) throw new IllegalArgumentException("Invalid order discount");
    }
}
