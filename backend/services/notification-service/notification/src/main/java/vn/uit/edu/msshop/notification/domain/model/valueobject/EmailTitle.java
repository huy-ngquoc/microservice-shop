package vn.uit.edu.msshop.notification.domain.model.valueobject;
public record EmailTitle(String value) {
    public EmailTitle {
        if(value==null||value.isBlank()) throw new IllegalArgumentException("Invalid email title");
    }
}
