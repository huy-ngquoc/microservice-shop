package vn.uit.edu.payment.domain.model.valueobject;

public record UserEmail(
        String value) {
    public UserEmail {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Invalid user email");
    }
}
