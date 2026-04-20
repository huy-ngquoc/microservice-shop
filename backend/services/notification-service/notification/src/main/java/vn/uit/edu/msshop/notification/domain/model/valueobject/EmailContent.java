package vn.uit.edu.msshop.notification.domain.model.valueobject;
public record EmailContent(String value) {
    public EmailContent {
        if(value==null||value.isBlank()) throw new IllegalArgumentException("Invalid email");
    }
}
