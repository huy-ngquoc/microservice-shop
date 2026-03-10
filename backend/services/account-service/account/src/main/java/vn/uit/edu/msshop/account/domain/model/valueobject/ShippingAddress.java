package vn.uit.edu.msshop.account.domain.model.valueobject;
public record ShippingAddress(String value) {
    public ShippingAddress {
        if(value==null||value.isBlank()) {
            throw new IllegalArgumentException("Invalid shipping address");
        }
    }

}
