package vn.uit.edu.msshop.inventory.domain.model.valueobject;
public record OrderStatus(String value) {
    public OrderStatus {
        if(value==null) throw new IllegalArgumentException("Invalid order status");
    }

}
