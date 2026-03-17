package vn.uit.edu.msshop.inventory.domain.model.valueobject;
public record OrderQuantity(int value) {
    public OrderQuantity {
        if(value<=0) throw new IllegalArgumentException("Invalid order quntity");
    }

}
