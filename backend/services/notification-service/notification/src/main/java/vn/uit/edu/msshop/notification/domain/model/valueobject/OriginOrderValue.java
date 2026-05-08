package vn.uit.edu.msshop.notification.domain.model.valueobject;
public record OriginOrderValue(int value) {
    public OriginOrderValue {
        if(value<=0) throw new IllegalArgumentException("Invalid oridin order value");
    }
}
