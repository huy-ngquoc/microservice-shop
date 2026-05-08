package vn.uit.edu.msshop.notification.domain.model.valueobject;
public record UserEmail(String value) {
    public UserEmail {
        if(value==null||value.isBlank()) throw new IllegalArgumentException("Invalid email");
    }
}
