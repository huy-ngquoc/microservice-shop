package vn.edu.uit.msshop.product.shared.application.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(
            final String message) {
        super(message);
    }

    public BusinessRuleException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
