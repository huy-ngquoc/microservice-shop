package vn.uit.edu.msshop.inventory.domain.model.valueobject;
public record Quantity(int value) {
    public Quantity {
        if(value<0) throw new IllegalArgumentException("Invalid quantity");
    }

}
