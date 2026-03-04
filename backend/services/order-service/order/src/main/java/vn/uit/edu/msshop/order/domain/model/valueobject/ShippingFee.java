package vn.uit.edu.msshop.order.domain.model.valueobject;
public record ShippingFee(long value) { 
    public ShippingFee {
        if(value<0) {
            throw new IllegalArgumentException("Invalid shipping fee");
        }
    }

}
