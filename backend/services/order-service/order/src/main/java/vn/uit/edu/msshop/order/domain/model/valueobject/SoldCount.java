package vn.uit.edu.msshop.order.domain.model.valueobject;
public record SoldCount(int value) {
    public SoldCount {
        if(value<0) throw new IllegalArgumentException("Invalid sold count");
    }
}
