package vn.uit.edu.msshop.inventory.domain.model.valueobject;
public record ReservedQuantity(int value) {
    public ReservedQuantity {
        if(value<0) throw new IllegalArgumentException("Invalid reserved quantity");
    }

}
