package vn.uit.edu.msshop.order.domain.model.valueobject;
public record OriginPrice(long value) {
    public OriginPrice {
        if(value<=0) {
            throw new IllegalArgumentException("Invalid origin price");
        }
    }
}
