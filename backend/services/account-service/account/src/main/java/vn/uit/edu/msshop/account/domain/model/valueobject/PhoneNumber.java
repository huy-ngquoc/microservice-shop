package vn.uit.edu.msshop.account.domain.model.valueobject;
public record PhoneNumber(String value) {
    public PhoneNumber{
        if(value==null||value.isBlank()) {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}
